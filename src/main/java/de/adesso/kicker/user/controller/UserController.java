package de.adesso.kicker.user.controller;

import de.adesso.kicker.user.persistence.Statistic;
import de.adesso.kicker.user.persistence.StatisticRepository;
import de.adesso.kicker.user.persistence.User;
import de.adesso.kicker.user.persistence.UserRepository;
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
    //Remove
    private final UserRepository userRepository;
    private final StatisticRepository statisticRepository;
    //above

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

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
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

    @GetMapping("/mail/notification")
    public boolean getEmailNotifications() {
        return userService.getEmailNotifications();
    }

    @GetMapping("/mail/toggle")
    public void toggleEmailNotifications() {
        userService.toggleEmailNotifications();
    }

    //TODO: Remove
    @GetMapping("/test/user")
    public void createTestUser() {
        User user1 = new User("test1", "Peter", "Schmalöer", "peter@online.de");
        User user2 = new User("test2", "Daniel", "Meier", "Daniel.Meier@adesso.de");
        User user3 = new User("test3", "Frederik", "Schlemmer", "Frederik.Schlemmer@adesso.de");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @GetMapping("/test/statistic")
    public void createTestStatistics() {
        Statistic statistic = new Statistic();
        statisticRepository.save(statistic);
    }

    @GetMapping("test/setstats")
    public void setStats() {
        User user1 = userRepository.findById("test1").get();
        user1.setStatistic(statisticRepository.findById("1").get());
        userRepository.save(user1);
        User user2 = userRepository.findById("test2").get();
        user2.setStatistic(statisticRepository.findById("2").get());
        userRepository.save(user2);
        User user3 = userRepository.findById("test3").get();
        user3.setStatistic(statisticRepository.findById("3").get());
        userRepository.save(user3);
    }
}
