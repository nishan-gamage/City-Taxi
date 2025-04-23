package com.esoft.citytaxi.config;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.entity.Operator;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.UserRole;
import com.esoft.citytaxi.entity.VehicleType;
import com.esoft.citytaxi.repository.OperatorRepository;
import com.esoft.citytaxi.repository.UserRoleRepository;
import com.esoft.citytaxi.repository.VehicleTypeRepository;
import com.esoft.citytaxi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MasterDataRunner {

    private final UserRoleRepository userRoleRepository;
    private final OperatorRepository operatorRepository;
    private final UserService userService;

    private final VehicleTypeRepository vehicleTypeRepository;

    public MasterDataRunner(UserRoleRepository userRoleRepository, OperatorRepository operatorRepository, UserService userService, VehicleTypeRepository vehicleTypeRepository) {
        this.userRoleRepository = userRoleRepository;
        this.operatorRepository = operatorRepository;
        this.userService = userService;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Bean
    public ApplicationRunner loadMasterData() {
        return args -> {
            insertIfNotExists("Operator", UserRoles.OPERATOR);
            insertIfNotExists("Driver", UserRoles.DRIVER);
            insertIfNotExists("Passenger", UserRoles.PASSENGER);
            insertOperatorIfNotExists();
            insertVehicleTypes();
        };
    }

    private void insertIfNotExists(String roleName, UserRoles roleCode) {
        Optional<UserRole> roleOptional = userRoleRepository.findByName(roleName);

        if (roleOptional.isEmpty()) {
            UserRole newRole = UserRole.builder()
                    .name(roleName)
                    .code(roleCode)
                    .build();
            userRoleRepository.save(newRole);
        }
    }

    @Value("${auth.operator.username}")
    String operatorUsername;

    @Value("${auth.operator.password}")
    String operatorPassword;

    @Value("${auth.operator.name}")
    String operatorName;

    private void insertOperatorIfNotExists() {
        Optional<User> user = this.userService.findByUsername(operatorUsername);
        if(user.isPresent()) {
            return;
        }
        Optional<UserRole> userRole = userRoleRepository.findByCode(UserRoles.OPERATOR);
        if(userRole.isEmpty()) {
            return;
        }
        User opsUser = User.builder()
                .username(operatorUsername)
                .password(operatorPassword)
                .role(userRole.get())
                .build();
        opsUser = this.userService.createUser(opsUser);
        Operator operator = Operator.builder().name(operatorName).user(opsUser).build();
        operatorRepository.save(operator);
    }

    private void insertVehicleTypes() {
        insertVehicleTypeIfNotExists("bike", "Motor Bike", 10.0);
        insertVehicleTypeIfNotExists("black", "Black Car", 15.0);
        insertVehicleTypeIfNotExists("lux", "Luxury Cab", 25.0);
        insertVehicleTypeIfNotExists("scooter", "Scooter", 5.0);
        insertVehicleTypeIfNotExists("suv", "SUV", 20.0);
        insertVehicleTypeIfNotExists("taxi", "Taxi Car", 12.0);
        insertVehicleTypeIfNotExists("tuktuk", "Tuk Tuk", 8.0);
        insertVehicleTypeIfNotExists("van", "Van", 18.0);
        insertVehicleTypeIfNotExists("x", "Cab X", 22.0);
        insertVehicleTypeIfNotExists("xl", "Cab XL", 30.0);
    }

    private void insertVehicleTypeIfNotExists(String code, String name, Double perKmCharge) {
        Optional<VehicleType> vehicleTypeOptional = vehicleTypeRepository.findByCode(code);
        if (vehicleTypeOptional.isEmpty()) {
            VehicleType newVehicleType = VehicleType.builder()
                    .code(code)
                    .name(name)
                    .perKmCharge(perKmCharge)
                    .build();
            vehicleTypeRepository.save(newVehicleType);
        }
    }
}
