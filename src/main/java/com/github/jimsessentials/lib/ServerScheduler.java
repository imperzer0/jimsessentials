package com.github.jimsessentials.lib;

import com.github.jimsessentials.JimsEssentials;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = JimsEssentials.MODID, value = Dist.DEDICATED_SERVER)
public class ServerScheduler
{
    private static ServerScheduler instance = null;

    private int tick_counter;
    private final List<JobInfo> jobs;

    /**
     * @apiNote This class follows strategy pattern
     */
    private ServerScheduler()
    {
        tick_counter = 0;
        jobs = new ArrayList<>();
    }

    /**
     * @apiNote Singleton instance
     */
    public static ServerScheduler Instance()
    {
        if (ServerScheduler.instance == null)
            ServerScheduler.instance = new ServerScheduler();

        return ServerScheduler.instance;
    }

    /**
     * @param jobInfo A Job to schedule
     * @apiNote Executed this job after specified amount of ticks. Use JobInfo.Builder to create an instance.
     */
    public ServerScheduler schedule(JobInfo jobInfo)
    {
        this.jobs.addLast(jobInfo);
        return this;
    }

    /**
     * @param jobInfo JobInfo instance with a filled out id
     * @apiNote Cancels a job with the same id
     */
    public ServerScheduler cancel(JobInfo jobInfo)
    {
        this.jobs.remove(jobInfo);
        return this;
    }

    /**
     * @param id Target job id
     * @apiNote Cancels a job by id
     */
    public ServerScheduler cancel(int id)
    {
        this.jobs.remove(new JobInfo(id));
        return this;
    }

    /**
     * @apiNote This method should never be called manually.
     * @apiNote Abuse will lead to inaccurate delay timings and will break many components using ServerScheduler
     */
    @SubscribeEvent
    private static void tick(ServerTickEvent tick)
    {
        ++Instance().tick_counter; // Start from 1

        int n = 0;
        for (JobInfo jobInfo : Instance().jobs)
        {
            if (jobInfo.delay == 0 || // Prevent division by 0
                    Instance().tick_counter % (jobInfo.delay + 1) == 0)
            {
                jobInfo.job.execute();
                Instance().jobs.remove(n);
            }

            ++n;
        }
    }


    public static class JobInfo
    {
        private static int ID_COUNTER = 0;
        private final int id;
        private int delay;
        private Job job;

        /**
         * @apiNote Instance created by this constructor is unusable
         */
        private JobInfo()
        {
            this.delay = -1;
            this.job = null;
            this.id = ID_COUNTER++;
        }

        /**
         * @param id Manually set id
         * @apiNote Used to find a job by its id.
         */
        private JobInfo(int id)
        {
            this.delay = -1;
            this.job = null;
            this.id = id;
        }

        /**
         * @return Job id
         */
        public int id()
        {
            return id;
        }

        /**
         * @apiNote Compares ids instead of entire objects
         */
        @Override
        public boolean equals(Object obj)
        {
            return obj instanceof JobInfo && id == ((JobInfo) obj).id;
        }

        /**
         * @return JobInfo Builder instance
         */
        public static Builder Builder()
        {
            return new Builder();
        }

        public static class Builder implements com.github.jimsessentials.lib.interfaces.Builder<JobInfo>
        {
            JobInfo instance;

            Builder()
            {
                instance = new JobInfo();
            }

            /**
             * @param delay Wait for this many ticks
             */
            public Builder delay(int delay)
            {
                this.instance.delay = delay;
                return this;
            }

            /**
             * @param job An implementation of ServerScheduler.JobInfo.Job interface
             */
            public Builder job(Job job)
            {
                this.instance.job = job;
                return this;
            }


            /**
             * @return JobInfo instance if delay and job parameters were set, and null otherwise.
             */
            @Override
            public JobInfo build()
            {
                if (this.instance.delay < 0)
                    return null;

                if (this.instance.job == null)
                    return null;

                return this.instance;
            }
        }
    }

    public interface Job
    {
        /**
         * @apiNote Implement this method to run the code you want
         */
        void execute();
    }
}
