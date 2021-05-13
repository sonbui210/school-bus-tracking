package com.hoanganhbk.schoolBus.api.student;

import com.hoanganhbk.schoolBus.app.entity.Response;
import com.hoanganhbk.schoolBus.data.model.Bus;
import com.hoanganhbk.schoolBus.data.model.Status;
import com.hoanganhbk.schoolBus.data.model.Student;
import com.hoanganhbk.schoolBus.data.model.StudentTracking;
import com.hoanganhbk.schoolBus.data.repository.BusRepository;
import com.hoanganhbk.schoolBus.data.repository.StudentRepository;
import com.hoanganhbk.schoolBus.data.repository.StudentTrackingRepository;
import com.hoanganhbk.schoolBus.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final BusRepository busRepository;
    private final StudentTrackingRepository studentTrackingRepository;

    @GetMapping
    public ResponseEntity findAll() {
        return Response.data(studentRepository.findAll(Sort.by("createAt")));
    }

    @PostMapping("/onChangeStatus")
    public ResponseEntity studentStatusChanged(@RequestBody String request) {
        JSONObject requestParsed = new JSONObject(request);
        Optional<Student> studentOpt = studentRepository.findById(requestParsed.optLong("id", 0));
        Student student;
        if (studentOpt.isPresent()) {
            student = studentOpt.get();

            student.setStatus(student.getStatus() == Status.ON_BUS ? Status.OFF_BUS : Status.ON_BUS);
        } else {
            student = new Student();
            student.setId(requestParsed.optLong("id", 0));
            student.setStatus(Status.ON_BUS);
        }

        String busCode = requestParsed.optString("busCode");
        Optional<Bus> busOptional = busRepository.findBusByCode(busCode);
        long busId = 0;
        Bus bus = null;
        if (busOptional.isPresent()) {
            bus = busOptional.get();
            busId = bus.getId();

            if (student.getStatus() == Status.ON_BUS) {
                student.setBus(bus);
            } else {
                student.setBus(null);
            }
        }

        {
            StudentTracking studentTracking = new StudentTracking();
            studentTracking.setStudentId(student.getId());
            studentTracking.setBusId(busId);
            studentTracking.setStatus(student.getStatus() == Status.ON_BUS ? 1 : 0);
            String action = student.getStatus() == Status.ON_BUS ? "lên xe" : "xuống xe";
            String now = DateUtil.format(new Date());
            studentTracking.setMessage("Học sinh " + student.getFullName() + " đã " + action + " " + (bus != null ? bus.getCode() : "unknown") + " lúc " + now);
            studentTracking.setCreateAt(now);
            studentTracking.setImage(requestParsed.optString("image"));
            studentTrackingRepository.save(studentTracking);
        }

        student = studentRepository.save(student);

        return Response.data(student);
    }

    @GetMapping("/findById")
    public ResponseEntity findStudentById(@RequestParam long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            return Response.data(studentOpt.get());
        } else {
            return Response.error(HttpStatus.NO_CONTENT, "Not found");
        }
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity createOrUpdateStudent(@RequestBody Student student) {
        student.setClassName(student.getClassName().replace(" ", "").toUpperCase());
        student = studentRepository.save(student);

        return Response.data(student);
    }

    @GetMapping("/search")
    public ResponseEntity searchStudent(@RequestParam String searchKey) {
        return Response.data(studentRepository.searchStudent(searchKey));
    }
}
