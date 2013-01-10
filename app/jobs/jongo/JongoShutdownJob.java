package jobs.jongo;

import jongo.Jongo;
import play.jobs.Job;
import play.jobs.OnApplicationStop;

@OnApplicationStop
public class JongoShutdownJob extends Job {

    @Override
    public void doJob() throws Exception {

        Jongo.shutdown();
    }
}
