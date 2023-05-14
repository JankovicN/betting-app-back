package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.repository.BetGroupRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BetGroupServiceImpl implements BetGroupService {
    private BetGroupRepository betGroupRepository;

    @Autowired
    public void setBetGroupRepository(BetGroupRepository betGroupRepository) {
        this.betGroupRepository = betGroupRepository;
    }

    @Override
    public BetGroup saveBetGroup(BetGroup betGroup) {
        return betGroupRepository.saveAndFlush(betGroup);
    }

    @Override
    public List<BetGroup> saveBetGroups(List<BetGroup> betGroups) {
        return betGroupRepository.saveAllAndFlush(betGroups);
    }

    @Override
    public List<BetGroup> getAllBetGroups() {
        return this.betGroupRepository.findAll();
    }

    @Override
    public BetGroup getBetGroupWithId(int betGroupId) {
        Optional<BetGroup> betGroup = betGroupRepository.findById(betGroupId);
        if (betGroup.isPresent()) {
            return betGroup.get();
        }
        return new BetGroup();
    }

    @Override
    public void deleteBetGroup(int id) {
        this.betGroupRepository.deleteById(id);
    }

    @Override
    public List<BetGroup> getBetGroupsByFixture(int fixture) {
        return betGroupRepository.findByOddsFixtureId(fixture);
    }

    @Override
    public long countRows() {
        return betGroupRepository.count();
    }

    @Override
    public boolean exists() {
        return betGroupRepository.count() > 0;
    }

    @Override
    public boolean existsWithId(int betGroupId) {
        return betGroupRepository.existsById(betGroupId);
    }

}
