package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Team;

@Service
public interface TeamService {
    Team save(Team team);
}
