package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.dto.vehicle.VehicleTypeResponseDto;
import com.esoft.citytaxi.service.SmsService;
import com.esoft.citytaxi.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService, SmsService smsService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/types")
    public ResponseEntity<AppResponse<List<VehicleTypeResponseDto>>> getAllTypes() {
        List<VehicleTypeResponseDto> reponseDtos = this.vehicleService.findAll()
                .stream().map(type -> VehicleTypeResponseDto.builder()
                        .id(type.getId())
                        .name(type.getName())
                        .code(type.getCode())
                        .perKmCharge(type.getPerKmCharge())
                        .build()).toList();
        AppResponse<List<VehicleTypeResponseDto>> body = new AppResponse<>();
        body.setMessage("Success");
        body.setData(reponseDtos);
        body.setStatusCode(HttpStatus.OK.value());
        body.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.ok(body);
    }
}
