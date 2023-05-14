package rs.ac.bg.fon.security.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.constants.AllBetGroups;
import rs.ac.bg.fon.constants.AllLeagues;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.service.BetGroupService;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.service.LeagueService;
import rs.ac.bg.fon.service.UserService;

import java.util.ArrayList;
import java.util.Date;

@Component
public class CommandLineRunnerStartUp implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerStartUp.class);
    private UserService userService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private FootballApiService footballApiService;
    private LeagueService leagueService;
    private BetGroupService betGroupService;
    private InitialAdminConfig initialAdminConfig;

    private AllLeagues allLeagues;
    private AllBetGroups allBetGroups;

    public CommandLineRunnerStartUp() {
    }

    @Override
    public void run(String... args) throws Exception {

        if (!leagueService.exists()) {
            leagueService.saveLeagues(allLeagues.getAllLeagues());
            logger.info("Successfully saved all leagues");
        }
        if (!betGroupService.exists()) {
            betGroupService.saveBetGroups(allBetGroups.getAllBetGroupsList());
            logger.info("Successfully saved all bet groups");
        }

        if (userRepository.existsByEmail(initialAdminConfig.getEmail())) return;

        userService.saveRole(new Role(null, "ROLE_CLIENT"));
        userService.saveRole(new Role(null, "ROLE_ADMIN"));
        userService.saveUser(new User(null, initialAdminConfig.getName(), initialAdminConfig.getSurname(),
                initialAdminConfig.getEmail(), new Date(1999, 06, 23), initialAdminConfig.getUsername(), initialAdminConfig.getPassword(),
                new ArrayList<>()));

        userService.addRoleToUser("janko", "ROLE_CLIENT");
        userService.addRoleToUser("janko", "ROLE_ADMIN");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFootballApiService(FootballApiService footballApiService) {
        this.footballApiService = footballApiService;
    }

    @Autowired
    public void setLeagueService(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @Autowired
    public void setAllLeagues(AllLeagues allLeagues) {
        this.allLeagues = allLeagues;
    }

    @Autowired
    public void setInitialAdminConfig(InitialAdminConfig initialAdminConfig) {
        this.initialAdminConfig = initialAdminConfig;
    }

    @Autowired
    public void setAllBetGroups(AllBetGroups allBetGroups) {
        this.allBetGroups = allBetGroups;
    }

    @Autowired
    public void setBetGroupService(BetGroupService betGroupService) {
        this.betGroupService = betGroupService;
    }
}
