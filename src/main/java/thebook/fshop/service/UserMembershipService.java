package thebook.fshop.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.UserMemberShip;
import thebook.fshop.helper.MemberType;
import thebook.fshop.repository.AccountsRepository;
import thebook.fshop.repository.UserMemberShipRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@EnableScheduling
public class UserMembershipService {
    UserMemberShipRepository userMemberShipRepository;
     AccountsRepository accountsRepository;

    @Scheduled(cron = "0 * * * * ?")
    public void updateMembershipStatus() {
        List<UserMemberShip> expiredMemberships = userMemberShipRepository.findByEndDateBefore(new Date());
        if (expiredMemberships.isEmpty()) {
            return;
        }
        expiredMemberships.forEach(userMemberShip -> {
            var account = userMemberShip.getAccount();
account.setMemberType(MemberType.NONE);
            accountsRepository.save(account);
            userMemberShipRepository.delete(userMemberShip);
        });
    }

}
