package rs.ac.uns.ftn.onlybunsapp.service;


import java.sql.Timestamp;
import java.util.Map;

public interface AnalyticsService {
    public Map<String, Integer> getActivityCounts(Timestamp startDate, Timestamp endDate);
    public Map<String, Double> getUserActivityStats();
}
