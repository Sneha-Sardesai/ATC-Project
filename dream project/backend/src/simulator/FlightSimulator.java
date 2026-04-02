package simulator;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import service.ATCService;

public class FlightSimulator {

    private ATCService atcService;
    private ScheduledExecutorService scheduler;
    private AtomicInteger flightIdCounter;
    private Random random;

    // We assume 3 generic aircraft models based on our seed data (101, 102, 103)
    private static final int[] AIRCRAFT_IDS = {101, 102, 103};

    public FlightSimulator(ATCService atcService) {
        this.atcService = atcService;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.random = new Random();
        
        // Get max flight ID from database and start from next + 100 to avoid conflicts
        try {
            dao.FlightDAO flightDAO = new dao.FlightDAO();
            int maxId = flightDAO.getMaxFlightId();
            this.flightIdCounter = new AtomicInteger(maxId + 100);
        } catch (Exception e) {
            System.err.println("Error getting max flight ID: " + e.getMessage());
            this.flightIdCounter = new AtomicInteger(1000); // fallback
        }
    }

    public void start() {
        System.out.println("Flight Simulator started. Generating new flights...");
        
        Runnable task = () -> {
            try {
                int newFlightId = flightIdCounter.getAndIncrement();
                int randomAircraftId = AIRCRAFT_IDS[random.nextInt(AIRCRAFT_IDS.length)];
                
                System.out.println("--- [SIMULATOR] New approaching flight detected on radar: " + newFlightId + " ---");
                atcService.systemAddFlight(newFlightId, randomAircraftId);
            } catch (Exception e) {
                System.err.println("Error generating flight: " + e.getMessage());
            }
        };

        // Schedule to run every 15 seconds
        scheduler.scheduleAtFixedRate(task, 5, 15, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Flight Simulator stopped.");
        }
    }
}
