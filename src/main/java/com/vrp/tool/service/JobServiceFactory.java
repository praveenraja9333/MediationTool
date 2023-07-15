package com.vrp.tool.service;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobServiceFactory {

    Map<String, Node> installedNodes = new HashMap<>();
    Set<Job> installedJobs = new HashSet<>();

    public Map<String,Node> getInstalledNodes() {
        return installedNodes;
    }
    public Set<Job> getInstalledJobs(){
        return installedJobs;
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
            nodes.stream().forEach(i->installedNodes.put(i.getName(),i));
        }

        @Override
        public void onRemove(List<Node> nodes) {
            nodes.stream().forEach(i->installedNodes.remove(i.getName(),i));
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

