package publications.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import publications.converter.PaymentConverter;
import publications.converter.PeriodicalConverter;
import publications.converter.SubscriptionConverter;
import publications.dto.SubscriptionDTO;
import publications.dto.UserDTO;
import publications.entity.Subscription;
import publications.entity.User;
import publications.converter.UserConverter;
import publications.dto.PeriodicalDTO;
import publications.dto.PaymentDTO;
import publications.service.PeriodicalService;
import publications.service.PaymentService;
import publications.service.SubscriptionService;
import publications.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final PeriodicalService periodicalService;
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    private final UserConverter userConverter;
    private final PeriodicalConverter periodicalConverter;
    private final SubscriptionConverter subscriptionConverter;
    private final PaymentConverter paymentConverter;

    @Autowired
    public UserController(UserService userService, PeriodicalService periodicalService,
                          SubscriptionService subscriptionService, PaymentService paymentService,
                          UserConverter userConverter, PeriodicalConverter periodicalConverter,
                          SubscriptionConverter subscriptionConverter, PaymentConverter paymentConverter) {
        this.userService = userService;
        this.periodicalService = periodicalService;
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
        this.userConverter = userConverter;
        this.periodicalConverter = periodicalConverter;
        this.subscriptionConverter = subscriptionConverter;
        this.paymentConverter = paymentConverter;
    }

    @RolesAllowed({ "admin" })
    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader String Authorization) {
        return ResponseEntity.ok(userConverter.listToDTO(userService.findAll()));
    }

    @RolesAllowed({ "admin", "user" })
    @GetMapping(value = "/periodicals")
    public ResponseEntity<List<PeriodicalDTO>> getAllPeriodicals(@RequestHeader String Authorization) {
        return ResponseEntity.ok(periodicalConverter.listToDTO(periodicalService.findAll()));
    }

    @RolesAllowed({ "admin", "user" })
    @PostMapping(value = "/periodicals")
    public void changePeriodicalAvailability(@RequestHeader String Authorization, @RequestBody long periodicalId,
                                             @RequestBody boolean available) {
        periodicalService.changePeriodicalAvailability(periodicalId, available);
    }

    @RolesAllowed({ "admin", "user" })
    @GetMapping(value = "/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(@RequestHeader String Authorization) {
        User user = userService.getUser(Authorization);

        if (user == null) {
            logger.warn("Cannot authorize user.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (user.getRole().equals("admin")) {
            return ResponseEntity.ok(subscriptionConverter.listToDTO(subscriptionService.findAll()));
        } else {
            return ResponseEntity.ok(subscriptionConverter.listToDTO(subscriptionService.findByUser(user)));
        }
    }

    @RolesAllowed({ "user" })
    @PostMapping(value = "/subscribe")
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestHeader String Authorization,
                                                     @RequestBody long periodicalId) {
        User user = userService.getUser(Authorization);

        if (user == null) {
            logger.warn("Cannot authorize user.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Subscription subscription = subscriptionService.subscribe(user.getId(), periodicalId);

        return ResponseEntity.ok(subscriptionConverter.toDTO(subscription));
    }

    @RolesAllowed({ "admin" })
    @PostMapping(value = "/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments(@RequestHeader String Authorization) {
        return ResponseEntity.ok(paymentConverter.listToDTO(paymentService.findAll()));
    }
}
