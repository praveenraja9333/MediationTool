package com.vrp.tool.service;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobServiceFactory {

    private Map<String, Node> installedNodes = new HashMap<>();
    private Set<Job> installedJobs = new HashSet<>();

    private List<Listener> subscribers = new LinkedList<>();

    public Subscriber subscriber = new Subscriber();


    public Map<String, Node> getInstalledNodes() {
        return installedNodes;
    }

    public Set<Job> getInstalledJobs() {
        return installedJobs;
    }

    public class Subscriber {
        public void addSubscribers(Listener listener) {
            subscribers.add(listener);
        }
    }


    public final Listener<Node> nodesListener = new Listener<Node>() {

        @Override
        public void onPublish(Node node) {
            installedNodes.put(node.getName(), node);

        }

        @Override
        public void onRemove(Node node) {
            installedNodes.remove(node.getName(), node);
        }

        @Override
        public void onPublish(List<Node> nodes) {
            nodes.stream().forEach(i -> installedNodes.put(i.getName(), i));
        }

        @Override
        public void onRemove(List<Node> nodes) {
            nodes.stream().forEach(i -> installedNodes.remove(i.getName(), i));
        }
    };
    public final Listener<Job> jobsListener = new Listener<Job>() {
        @Override
        public void onPublish(Job node) {
            installedJobs.add(node);
            //addJob
        }

        @Override
        public void onRemove(Job node) {
            installedJobs.remove(node);
            //removeJob
        }

        @Override
        public void onPublish(List<Job> T) {
            installedJobs.addAll(T);
            //removeJobs
        }

        @Override
        public void onRemove(List<Job> T) {
            installedJobs.removeAll(T);
            //removeJobs
        }
    };
}

