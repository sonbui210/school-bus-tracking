package com.hoanganhbk.schoolBus.util;

import com.hoanganhbk.schoolBus.api.bus.BusDto;
import com.hoanganhbk.schoolBus.data.model.Bus;
import com.hoanganhbk.schoolBus.data.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusUtil {
    private final BusRepository busRepository;

    public String validateBusInfo(BusDto busDto) {
        if (busDto.getId() != null && busDto.getId() != 0) {
            Bus bus = busRepository.findById(busDto.getId()).get();

            if (!bus.getCode().equals(busDto.getCode())) {
                String busCode = busDto.getCode();
                if (busRepository.findBusByCode(busCode).isPresent()) {
                    return "Mã xe đã tồn tại!";
                }
            }

            if (!bus.getLicensePlate().equals(busDto.getLicensePlate())) {
                String licensePlate = busDto.getLicensePlate();
                if (busRepository.findByLicensePlate(licensePlate).isPresent()) {
                    return "Biển số xe đã tồn tại!";
                }
            }

        } else {
            String busCode = busDto.getCode();
            if (busRepository.findBusByCode(busCode).isPresent()) {
                return "Mã xe đã tồn tại!";
            }

            String licensePlate = busDto.getLicensePlate();
            if (busRepository.findByLicensePlate(licensePlate).isPresent()) {
                return "Biển số xe đã tồn tại!";
            }
        }

        return "";
    }
}
