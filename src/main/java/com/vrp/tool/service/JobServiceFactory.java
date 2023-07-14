package com.vrp.tool.service;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JobServiceFactory {

    Set<Node> installedNodes = new HashSet<>();
    Set<Job> installedJobs = new HashSet<>();

    public Set<Node> getInstalledNodes() {
        return installedNodes;
    }

    public final Listener<Node> nodesListener = new Listener<Node>() {
        @Override
        public void onPublish(Node node) {
            installedNodes.add(node);
        }

        @Override
        public void onRemove(Node node) {
            installedNodes.remove(node);
        }

        @Override
        public void onPublish(List<Node> T) {
            installedNodes.addAll(T);
        }

        @Override
        public void onRemove(List<Node> T) {
            installedNodes.removeAll(T);
        }
    };
    public final Listener<Job> jobsListener = new Listener<Job>() {
        @Override
        public void onPublish(Job node) {
            installedJobs.add(node);
        }

        @Override
        public void onRemove(Job node) {
            installedJobs.remove(node);
        }

        @Override
        public void onPublish(List<Job> T) {
            installedJobs.addAll(T);
        }

        @Override
        public void onRemove(List<Job> T) {
            installedJobs.removeAll(T);
        }
    };


}
