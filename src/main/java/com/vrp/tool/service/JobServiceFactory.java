package com.vrp.tool.service;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class JobServiceFactory {

    private final Map<String, Node> installedNodes = new ConcurrentHashMap<>();
    private final Set<Job> installedJobs = new HashSet<>();

    private final List<Listener> subscribers = new LinkedList<>();

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
            jobsListener.onRemove(installedJobs.stream().filter(i->node.equals(i.getNode())).collect(Collectors.toList()));
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
            for(Listener listener:subscribers){
                listener.onPublish(node);
            }
        }

        @Override
        public void onRemove(Job job) {
            installedJobs.remove(job);
            for(Listener listener:subscribers){
                listener.onRemove(job);
            }
        }

        @Override
        public void onPublish(List<Job> jobs) {
            installedJobs.addAll(jobs);
            //removeJobs
        }

        @Override
        public void onRemove(List<Job> jobs) {
            installedJobs.removeAll(jobs);
            for (Job _job : jobs){
                for (Listener listener : subscribers) {
                    listener.onRemove(_job);
                }
            }
        }
    };
}

