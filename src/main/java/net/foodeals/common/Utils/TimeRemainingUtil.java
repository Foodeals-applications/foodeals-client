package net.foodeals.common.Utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeRemainingUtil {

    public static String getTimeRemaining(Date date, String to) {
        // 1. Convertir Date vers LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 2. Parser l'heure de fin (to) supposée au format "HH:mm" ou "HH:mm:ss"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endTime = LocalTime.parse(to, timeFormatter);

        // 3. Créer l'objet LocalDateTime de fin
        LocalDateTime endDateTime = LocalDateTime.of(localDate, endTime);

        // 4. Prendre maintenant
        LocalDateTime now = LocalDateTime.now();

        // 5. Calculer la durée
        if (now.isAfter(endDateTime)) {
            return "Offre expirée";
        }

        Duration duration = Duration.between(now, endDateTime);
        long totalSeconds = duration.getSeconds();

        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%dj%dh%02dmin%02ds", days, hours, minutes, seconds);
    }
}
