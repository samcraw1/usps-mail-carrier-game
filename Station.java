public class Station {

    String name;
    int carriersOnRoute;
    int packagesDelivered;
    int packagesRemaining;
    int grievancesFiled;
    int llvsActive;

    public Station(String name, int carriersOnRoute, int packagesDelivered,
                   int packagesRemaining, int grievancesFiled, int llvsActive) {
        this.name = name;
        this.carriersOnRoute = carriersOnRoute;
        this.packagesDelivered = packagesDelivered;
        this.packagesRemaining = packagesRemaining;
        this.grievancesFiled = grievancesFiled;
        this.llvsActive = llvsActive;
    }

    public double getDeliveryRatio() {
            int total = packagesDelivered + packagesRemaining;
            if (total == 0) return 1.0;
            return (double) packagesDelivered / total;
    }

    public String getStatus() {
        if (grievancesFiled >= 3) return "CRITICAL";
        double ratio = getDeliveryRatio();
        if (ratio >= 0.75) return "ON TRACK";
        if (ratio >= 0.50) return "BEHIND";
        return "CRITICAL";
    }
    

}
