package jobs.jongo;

import jongo.Jongo;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class JongoInitJob extends Job {

    @Override
    public void doJob() throws Exception {

        Jongo.init();
    }
}
