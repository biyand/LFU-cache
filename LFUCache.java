public class LFUCache {
    private KeyNode head, tail;
    final int capacity;
    private HashMap<Integer, Integer> valueMap;
    private HashMap<Integer, KeyNode> nodeMap;
    public LFUCache(int capacity) {
        this.capacity = capacity;
        valueMap = new HashMap<Integer, Integer>();
        nodeMap = new HashMap<Integer, KeyNode>();
    }
    
    public int get(int key) {
        if(valueMap.containsKey(key)){
            freqIncrease(key);
            return valueMap.get(key);
        }
        return -1;
    }
    
    private void freqIncrease(int key){
        KeyNode keyNode = nodeMap.get(key);
        int newFreq=keyNode.freq+1;
        keyNode.keys.remove(key);
        if(keyNode.next == null){
            keyNode.next = new KeyNode(keyNode,null, newFreq, key);
        } else 
        if(keyNode.next.freq != newFreq){
            KeyNode newKeyNode = new KeyNode(keyNode,keyNode.next, newFreq,key);
            keyNode.next.pre = newKeyNode;
            keyNode.next = newKeyNode;
        } else {
        keyNode.next.keys.add(key);}
        nodeMap.put(key, keyNode.next);
        if(keyNode.keys.size() == 0){
            removeNode(keyNode);
        }
        return;
    }
    
    public void put(int key, int value) {
        if(this.capacity == 0)
            return;
        if(valueMap.containsKey(key)){
            valueMap.put(key, value);
            freqIncrease(key);
            return;
        }
        if(valueMap.size() == this.capacity)
            removeLFU();
        valueMap.put(key, value);
        if(head==null || head.freq > 1){
            KeyNode newNode = new KeyNode(null, head, 1, key);
            if(head != null)
                head.pre = newNode;
            head = newNode;
        } else{
            head.keys.add(key);
        }
        nodeMap.put(key, head);
    }
    
    public void removeNode(KeyNode keyNode){
        if(head == keyNode)
            head = keyNode.next;
        else 
            keyNode.pre.next = keyNode.next;
        if(keyNode.next != null)
            keyNode.next.pre = keyNode.pre;
    }
    
    public void removeLFU(){
        if(head == null)
            return;
        int toRemove = head.keys.iterator().next();
        head.keys.remove(toRemove);
        if(head.keys.isEmpty())
            removeNode(head);
        nodeMap.remove(toRemove);
        valueMap.remove(toRemove);
    }
    
    class KeyNode{
        public KeyNode pre, next;
        public final int freq;
        public LinkedHashSet<Integer> keys = new LinkedHashSet<Integer>();
        public KeyNode(KeyNode pre, KeyNode next, int freq, int key){
            this.pre = pre;
            this.next = next;
            this.freq = freq;
            keys.add(key);
        }
    }
}
