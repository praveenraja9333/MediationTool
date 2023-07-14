package com.vrp.tool.configurator;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.flow.publishers.Publisher;
import com.vrp.tool.models.Node;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class NodePublisher implements Publisher<Node> {

    List<Listener>subscribers=new LinkedList<>();

    public boolean addSubscribers(Listener listener){
        if(subscribers.contains(listener))return false;
        subscribers.add(listener);
        return true;
    }

    @Override
    public void published(Node node) {
          for(Listener listener: subscribers){
              listener.onPublish(node);
          }
    }

    @Override
    public void removed(Node node) {
        for(Listener listener: subscribers){
            listener.onRemove(node);
        }

    }

    @Override
    public void removed(List<Node> nodes) {
        for(Listener listener: subscribers) {
            listener.onRemove(nodes);
        }
    }

    @Override
    public void published(List<Node> nodes) {
        for(Listener listener: subscribers) {
            listener.onPublish(nodes);
        }
    }
}
