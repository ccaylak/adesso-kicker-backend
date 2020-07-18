package de.adesso.kicker.user.service;

import de.adesso.kicker.user.exception.UserNotFoundException;
import de.adesso.kicker.user.persistence.User;
import de.adesso.kicker.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StatisticsService statisticsService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public List<User> getAllUsersWithStatistics() {
        return new ArrayList<>(userRepository.findAllByStatisticNotNull());
    }

    public List<User> getUsersSortedByRating(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("statistic.rating").descending());
        return userRepository.findAllByStatisticNotNull(pageable).getContent();
    }

    public Page<User> getUserPageSortedByRating(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("statistic.rating").descending());
        return userRepository.findAllByStatisticNotNull(pageRequest);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getLoggedInUser() {
        return getUserById(visitingUserId());
    }

    private String visitingUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void createUser(Authentication authentication) {
        var userAccessToken = getAccessToken(authentication);
        var userId = userAccessToken.getPreferredUsername();
        var firstName = userAccessToken.getGivenName();
        var lastName = userAccessToken.getFamilyName();
        var email = userAccessToken.getEmail();
        User user = new User(userId, firstName, lastName, email);
        saveUser(user);
        logger.info("User {} has been created", user.getUserId());
    }

    private AccessToken getAccessToken(Authentication authentication) {
        var simpleKeycloakAccount = (SimpleKeycloakAccount) authentication.getDetails();
        return simpleKeycloakAccount.getKeycloakSecurityContext().getToken();
    }

    @EventListener
    public void checkFirstLogin(AuthenticationSuccessEvent event) {
        var authentication = event.getAuthentication();
        var principal = (KeycloakPrincipal) authentication.getPrincipal();
        if (!checkUserExists(principal.getName())) {
            createUser(authentication);
        }
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private boolean checkUserExists(String id) {
        return userRepository.existsById(id);
    }

    public void deleteAllStatistics() {
        var users = getAllUsersWithStatistics();
        users.forEach(user -> user.setStatistic(null));
        saveAllUsers(users);
        statisticsService.deleteAll();
    }

    public void toggleEmailNotifications() {
        var user = getLoggedInUser();
        user.setEmailNotifications(!user.isEmailNotifications());
        saveUser(user);
    }

    public boolean getEmailNotifications() {
        return getLoggedInUser().isEmailNotifications();
    }

    private void saveAllUsers(Iterable<User> users) {
        userRepository.saveAll(users);
    }
}
