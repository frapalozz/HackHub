package unicam.hackhub.infrastructure.services.payment;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.payment.PaymentHandler;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
@AllArgsConstructor
public class MockPaymentAdapter implements PaymentHandler {

    private final TeamRepository teamRepository;

    @Override
    public void transferPrize(double amount, String teamName) {
        Team team = getTeam(teamName);

        team.increaseBalance(amount);

        teamRepository.save(team);
    }

    private Team getTeam(String teamName) {
        Team team = teamRepository.findById(teamName).orElse(null);

        if(team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        return team;
    }
}
