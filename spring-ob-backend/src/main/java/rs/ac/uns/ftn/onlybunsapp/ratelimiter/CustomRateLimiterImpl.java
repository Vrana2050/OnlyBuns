package rs.ac.uns.ftn.onlybunsapp.ratelimiter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomRateLimiterImpl {
    private int maxRequests;
    private int durationInSeconds;
    private List<Long> timeStamps = new ArrayList<>();

    public CustomRateLimiterImpl(int maxRequests, int durationInSeconds) {
        this.maxRequests = maxRequests;
        this.durationInSeconds = durationInSeconds;
    }

    public boolean IsAllowed()
    {
        ClearTimeStamps();
        if(timeStamps.size() >= maxRequests)
            return false;
        timeStamps.add(System.currentTimeMillis());
        return true;
    }
    private void ClearTimeStamps(){
        if(timeStamps.size()>0 && timeStamps.get(0)<System.currentTimeMillis()-durationInSeconds* 1000L)
        {
            timeStamps.clear();
        }
    }

}
