package JAVAC5.com.WebOrderDoAn.Services;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LuckyWheelService {

    public String spinWheel(HttpSession session) {
        // List of prizes
        String[] prizes = {"10% Discount", "Free Drink", "Buy 1 Get 1 Free", "Free Dessert", "Better Luck Next Time"};

        // Spin the wheel
        Random random = new Random();
        int prizeIndex = random.nextInt(prizes.length);

        // Get the prize
        String prize = prizes[prizeIndex];

        // Store the prize in session (if needed)
        session.setAttribute("luckyWheelPrize", prize);

        return prize;
    }
}