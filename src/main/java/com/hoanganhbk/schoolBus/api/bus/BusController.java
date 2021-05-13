package com.hoanganhbk.schoolBus.api.bus;


import com.hoanganhbk.schoolBus.app.entity.AppException;
import com.hoanganhbk.schoolBus.app.entity.Response;
import com.hoanganhbk.schoolBus.data.model.Bus;
import com.hoanganhbk.schoolBus.data.repository.BusRepository;
import com.hoanganhbk.schoolBus.helper.WebsocketHelper;
import com.hoanganhbk.schoolBus.util.BusUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusRepository busRepository;
    private final WebsocketHelper websocketHelper;
    private final BusUtil busUtil;

    @GetMapping("/searchPlace")
    public ResponseEntity searchPlace(@RequestParam String query) {
        String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity httpEntity = new HttpEntity(headers, null);

        URL = URL + query + "&key=AIzaSyDwCWIfMN-7ZSnd01VsYgB19NIL3352vJI";

        ResponseEntity<String> response = restTemplate.exchange(URL,  HttpMethod.GET, httpEntity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        return Response.dataJson( result);
    }

    @PostMapping("/addNewBus")
    public ResponseEntity addNewBus(@RequestBody BusDto busDto) {
        String error = busUtil.validateBusInfo(busDto);
        if (!StringUtils.isEmpty(error)) {
            throw new AppException(error);
        }

        Bus bus = busRepository.findById(busDto.getId()).orElseGet(Bus::new);

        bus.setCode(busDto.getCode());
        bus.setDriver(busDto.getDriver());
        bus.setLicensePlate(busDto.getLicensePlate());
        bus.setPhone(busDto.getPhone());

        JSONObject route = new JSONObject(busDto.getRoute());
        bus.setRoute(route.toString());
        bus.setStatus("OFF");
        bus.setWaypoint(busDto.getWaypoint());

        bus = busRepository.save(bus);

        return Response.data(bus);
    }

    @GetMapping
    public ResponseEntity getAllBuses() {
        return Response.data(busRepository.findAll());
    }

    @PostMapping("/positionUpdated")
    public ResponseEntity updatePosition(@RequestBody PositionDto request) {
        Optional<Bus> busOpt = busRepository.findById(request.getBusId());
        if (busOpt.isPresent()) {
            Bus bus = busOpt.get();

            bus.setCurrentLat(request.getLat());
            bus.setCurrentLng(request.getLng());

            bus = busRepository.save(bus);

            websocketHelper.pushBusEvent(request);
        }

        return Response.data("Bus's position was updated");
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam long busId) {
        Optional<Bus> busOpt = busRepository.findById(busId);
        if (busOpt.isPresent()) {
            Bus bus = busOpt.get();

            return Response.data(bus);
        } else {
            throw new AppException("Bus does not exist");
        }
    }

    @GetMapping("/deleteById")
    public ResponseEntity deleteBusById(@RequestParam long busId) {
        Optional<Bus> busOpt = busRepository.findById(busId);
        if (busOpt.isPresent()) {
            Bus bus = busOpt.get();
            busRepository.delete(bus);
            return Response.data(busId + " was deleted");
        } else {
            throw new AppException("Bus does not exist");
        }
    }

    @GetMapping("/search")
    public ResponseEntity searchBus(@RequestParam String searchKey) {
        return Response.data(busRepository.findByCodeLike(searchKey));
    }
}
