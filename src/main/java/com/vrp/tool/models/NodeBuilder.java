package com.vrp.tool.models;

public class NodeBuilder implements Builder<NodeBuilder>{
    String name;
    String srcDirectory;
    String dstDirectory;

    public NodeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NodeBuilder setSrcDirectory(String srcDirectory) {
        this.srcDirectory = srcDirectory;
        return this;
    }

    public NodeBuilder setDstDirectory(String dstDirectory) {
        this.dstDirectory = dstDirectory;
        return this;
    }


    public static NodeBuilder newBuilder() {
        return new NodeBuilder();
    }


    public static NodeBuilder newBuilder(NodeBuilder nodeBuilder) {
        return nodeBuilder;
    }

    @Override
    public Node build(NodeBuilder nodeBuilder) {
        Node node=new Node();
        node.setName(nodeBuilder.name);
        node.setSrcDirectory(nodeBuilder.srcDirectory);
        node.setDstDirectory(nodeBuilder.dstDirectory);
        return node;
    }

    @Override
    public Node build() {
         return build(this);
    }

}
