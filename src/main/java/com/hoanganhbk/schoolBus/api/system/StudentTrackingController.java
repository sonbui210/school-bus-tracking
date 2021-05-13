package com.hoanganhbk.schoolBus.api.system;


import com.hoanganhbk.schoolBus.api.system.payload.DoFilterRequest;
import com.hoanganhbk.schoolBus.app.entity.Response;
import com.hoanganhbk.schoolBus.data.model.StudentTracking;
import com.hoanganhbk.schoolBus.data.repository.StudentTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/studentTracking")
@Slf4j
@RequiredArgsConstructor
public class StudentTrackingController {
    private final StudentTrackingRepository studentTrackingRepository;
    private final EntityManager em;

    @GetMapping
    public ResponseEntity getAllTracking(@RequestParam int page, @RequestParam int pageSize) {
        return Response.data(studentTrackingRepository.findAllOrderByCreateAtDesc(PageRequest.of(page, pageSize))); 
    }

    @PostMapping
    public ResponseEntity doFilter(@RequestBody DoFilterRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StudentTracking> cq = cb.createQuery(StudentTracking.class);
        Root<StudentTracking> studentTrackingRoot = cq.from(StudentTracking.class);

        List<Predicate> predicateList = new ArrayList<>();
        if (!request.getSearchKey().isEmpty()) {
            Predicate messagePredicate = cb.like(studentTrackingRoot.get("message"), "%" + request.getSearchKey() + "%");
            predicateList.add(messagePredicate);
        }

        if (!"all".equals(request.getStatus())) {
            int status = 0;
            if ("on".equals(request.getStatus())) {
                status = 1;
            }

            Predicate statusPredicate = cb.equal(studentTrackingRoot.get("status"), status);
            predicateList.add(statusPredicate);
        }

        cq.where(predicateList.stream().toArray(Predicate[]::new));
        cq.orderBy(cb.desc(studentTrackingRoot.get("createAt")));

        List<StudentTracking> result = em.createQuery(cq).setFirstResult(request.getPage()).setMaxResults(request.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StudentTracking> booksRootCount = countQuery.from(StudentTracking.class);
        countQuery.select(cb.count(booksRootCount)).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
        Long count = em.createQuery(countQuery).getSingleResult();

        Page<StudentTracking> finalResult = new PageImpl<>(result, PageRequest.of(request.getPage() - 1, request.getPageSize()), count);


        return Response.data(finalResult);
    }
}
