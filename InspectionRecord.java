public class InspectionRecord {

    private String status;
    private String summary;
    private String createdAt;


    public InspectionRecord(String status, String summary, String createdAt) {
        this.status = status;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }
    public String getSummary() {
        return summary;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
