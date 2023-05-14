package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.BetGroup;

import java.util.List;


public interface BetGroupService {

    BetGroup saveBetGroup(BetGroup betGroup);

    List<BetGroup> saveBetGroups(List<BetGroup> betGroups);

    List<BetGroup> getAllBetGroups();

    BetGroup getBetGroupWithId(int betGroupId);
    List<BetGroup> getBetGroupsByFixture(int fixture);

    long countRows();

    boolean exists();

    boolean existsWithId(int betGroupId);

    void deleteBetGroup(int id);

}
