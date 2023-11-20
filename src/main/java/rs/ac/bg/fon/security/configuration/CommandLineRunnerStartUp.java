package rs.ac.bg.fon.security.configuration;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.constants.AllBetGroups;
import rs.ac.bg.fon.constants.AllLeagues;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.service.BetGroupService;
import rs.ac.bg.fon.service.LeagueService;
import rs.ac.bg.fon.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;

@NoArgsConstructor
@Component
public class CommandLineRunnerStartUp implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerStartUp.class);
    @Autowired

    public AllLeagues allLeagues;
    @Autowired
    public AllBetGroups allBetGroups;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LeagueService leagueService;
    @Autowired
    BetGroupService betGroupService;
    @Autowired
    InitialAdminConfig initialAdminConfig;

    @Override
    public void run(String... args) throws Exception {

        if (!leagueService.exists()) {
            leagueService.saveLeagues(allLeagues.getAllLeagues());
        }
        if (!betGroupService.exists()) {
            betGroupService.saveBetGroups(allBetGroups.getAllBetGroupsList());
            logger.info("Successfully saved all bet groups");
        }

        if (userRepository.existsByEmail(initialAdminConfig.getEmail())) return;

        userService.saveRole(new Role(null, "ROLE_CLIENT"));
        userService.saveRole(new Role(null, "ROLE_ADMIN"));
        userService.saveUser(new User(null, initialAdminConfig.getName(), initialAdminConfig.getSurname(),
                initialAdminConfig.getEmail(), LocalDate.of(1999, 6, 23), initialAdminConfig.getUsername(), initialAdminConfig.getPassword(),
                new ArrayList<>()));

        userService.addRoleToUser("janko", "ROLE_CLIENT");
        userService.addRoleToUser("janko", "ROLE_ADMIN");
    }

}
