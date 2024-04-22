package pro.sky.telegrambot.timer;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.NotificationTaskService;
import pro.sky.telegrambot.service.TelegramBotService;

@Component
public class NotificationTimer {

    private final NotificationTaskService notificationTaskService;
    private final TelegramBotService telegramBotService;

    public NotificationTimer(NotificationTaskService notificationTaskService,
                             TelegramBotService telegramBotService) {
        this.notificationTaskService = notificationTaskService;
        this.telegramBotService = telegramBotService;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkNotifications() {
        notificationTaskService.findNotificationsForSend().forEach(notificationTask -> {
            telegramBotService.sendMessage(
                    notificationTask.getUserId(),
                    "Вы просили напомнить об этом: " + notificationTask.getMessage()
            );
            notificationTaskService.deleteTask(notificationTask);
        });
    }

}
