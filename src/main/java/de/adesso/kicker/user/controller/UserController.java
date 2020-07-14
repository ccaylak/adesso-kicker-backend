package de.adesso.kicker.user.controller;

import de.adesso.kicker.user.persistence.User;
import de.adesso.kicker.user.service.UserService;
import de.adesso.kicker.user.trackedstatistic.persistence.TrackedStatistic;
import de.adesso.kicker.user.trackedstatistic.service.TrackedStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final TrackedStatisticService trackedStatisticService;

    @GetMapping("/u/{id}")
    public User getUserProfile(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/you")
    public User getOwnProfile() {
        return userService.getLoggedInUser();
    }

    @GetMapping("/statistics/{id}")
    public List<TrackedStatistic> getTrackedStatistics(@PathVariable String id) {
        var user = userService.getUserById(id);
        return trackedStatisticService.getTrackedStatisticsByUser(user);
    }

    @GetMapping("/ranking/all")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getUserPageSortedByRating(pageable.getPageNumber(), pageable.getPageSize());
    }

    @GetMapping("/mail/toggle")
    public void toggleMail() {
        userService.changeEmailNotifications();
    }
}
