package indie.tango.defuse.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class GameSession {
    private final Map<String, String> playerSessions = new ConcurrentHashMap<>();
    private final Map<String, Boolean> activeGames = new ConcurrentHashMap<>();
    private final Map<String, Integer> gameTimers = new ConcurrentHashMap<>();
    private Map<String, ScheduledFuture<?>> timerTasks = new ConcurrentHashMap<>();
    private Map<String, List<Constants.Scenario>> stepsTaken = new ConcurrentHashMap<>();
    private Map<String, Integer> stepsCount = new ConcurrentHashMap<>();
    private Map<String, Integer> countMistake = new ConcurrentHashMap<>();

    private final Map<String, String> playerOnline = new ConcurrentHashMap<>();

    private GameMod gameMod;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private TaskScheduler taskScheduler;

    public GameMod getGameMod() {
        return gameMod;
    }

    public void setGameMod(String gameMod) {
        switch (gameMod) {
            case ("easy"):
                this.gameMod = GameMod.EASY;
                break;
            case ("normal"):
                this.gameMod = GameMod.NORMAL;
                break;
            case ("hard"):
                this.gameMod = GameMod.HARD;
                break;
        }
    }

    public String generateGameCode() {
        String gameCode = generateRandomCode();
        activeGames.put(gameCode, true); // Позначити гру як активну
        return gameCode;
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        return String.valueOf(code);
    }

    public void addPlayerSession(String gameCode, String playerSessionId, String userName) {
        playerSessions.put(playerSessionId, gameCode);
        playerOnline.put(userName, gameCode);
    }

    public void removeOnlinePlayer(String userName) {
        this.playerOnline.remove(userName);
    }

    public void initStepsCount(String gameCode) {
        this.stepsCount.put(gameCode, 0);
    }
    public void initCountsError(String gameCode) {
        this.countMistake.put(gameCode, 0);
    }

    public String getGameCodeForPlayer(String playerSessionId) {
        return playerSessions.get(playerSessionId);
    }

    public List<String> getPlayerSessions(String gameCode) {
        List<String> sessions = new ArrayList<>();
        for (Map.Entry<String, String> entry : playerSessions.entrySet()) {
            if (entry.getValue().equals(gameCode)) {
                sessions.add(entry.getKey());
            }
        }
        return sessions;
    }

    public boolean isGameExists(String gameCode) {
        return activeGames.containsKey(gameCode);
    }

    public void setTimer(String gameCode) {
        gameTimers.put(gameCode, gameMod.getTime());
    }

    public int decrementTimer(String gameCode) {
        return gameTimers.compute(gameCode, (key, value) -> value != null && value > 0 ? value - 1 : 0);
    }

    public Map<String, ScheduledFuture<?>> getTimerTasks() {
        return timerTasks;
    }

    public void setTimerTasks(Map<String, ScheduledFuture<?>> timerTasks) {
        this.timerTasks = timerTasks;
    }

    public void stopTimer(String gameCode) {
        ScheduledFuture<?> timerTask = timerTasks.get(gameCode);
        if (timerTask != null && !timerTask.isCancelled()) {
            timerTask.cancel(false);
            timerTasks.remove(gameCode);
        }
    }

    public String doStep(String gameCode, String button) {
        int scenarioNumber = Integer.parseInt(gameCode.substring(gameCode.length() - 1));
        String result;
        if (Constants.scenarios.get(scenarioNumber).steps.get(this.stepsCount.get(gameCode)).equals(button)) {
            if (Constants.scenarios.get(scenarioNumber).steps.size() == this.stepsCount.get(gameCode) + 1) {
                result = "win";
            } else {
                result = "true";
            }
        } else {
            this.countMistake.put(gameCode, this.countMistake.get(gameCode) + 1);
            result = "false";
        }
        if (this.countMistake.get(gameCode) > this.gameMod.getCountMistake()) {
            result = "lose";
        }
        this.stepsCount.put(gameCode,this.stepsCount.get(gameCode) + 1);
        return result;
    }

    public Map<String, String> filterOnlineFriends(List<String> allFriends) {
        Map<String, String> onlineFriends = new HashMap<>();

        for (String email : allFriends) {
            // Перевірка, чи є друг онлайн
            if (this.playerOnline.containsKey(email)) {
                onlineFriends.put(email, this.playerOnline.get(email));
            }
        }
        return onlineFriends;
    }
}
