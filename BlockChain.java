import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farhan on 1/31/2018.
 */
public class BlockChain {

    public static void main(String[] args) {

        MyBlockChain blockChain = new MyBlockChain();
        System.out.println("Mining Block 1...");
        blockChain.addBlock(new Block(1, "Hi", "2/1/18", ""));
        System.out.println("Mining Block 2...");
        blockChain.addBlock(new Block(2, "Hey", "3/1/18", ""));

		/*To simply print & check the validity of blockchain*/
        /*Gson gson = new Gson();
        String json = gson.toJson(blockChain);
        System.out.println(json);
        System.out.println(blockChain.isChainValid());*/

        /*To successful temper, go through all previous nodes from the tempered one to re-calculate all hashes*/
       /* blockChain.myChain.get(1).data = "tempered";
        blockChain.myChain.get(1).hash = blockChain.myChain.get(1).calculateHash();
        blockChain.myChain.get(1).previousHash = blockChain.myChain.get(0).calculateHash();
        blockChain.myChain.get(2).previousHash = blockChain.myChain.get(1).calculateHash();
        blockChain.myChain.get(2).hash = blockChain.myChain.get(2).calculateHash();
        System.out.println(blockChain.isChainValid());*/
    }

}

class MyBlockChain {
    List<Block> myChain = new ArrayList<>();
    int difficulty;

    MyBlockChain() {
        this.myChain.add(this.createGenesisBlock());
        this.difficulty = 5;
    }

    Block createGenesisBlock() {
        return new Block(0, "hello", "1/1/18", "0");
    }

    Block getLatestBlock() {
        return myChain.get(myChain.size() - 1);
    }

    void addBlock(Block newBlock) {
        newBlock.previousHash = this.getLatestBlock().hash;
        //newBlock.hash = newBlock.calculateHash();
        /*level up: use mine block instead :-)*/
        newBlock.mineBlock(difficulty);
        this.myChain.add(newBlock);
    }

    boolean isChainValid() {
        for (int i = 1; i < myChain.size(); i++) {
            Block currentBlock = myChain.get(i);
            Block previousBlock = myChain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash()))
                return false;

            if (!currentBlock.previousHash.equals(previousBlock.hash))
                return false;

        }
        return true;
    }
}

class Block {
    int index;
    String data;
    String timeStamp;
    String previousHash;
    String hash;
    int nonce;

    Block(int index, String data, String timeStamp, String previousHash) {
        this.index = index;
        this.data = data;
        this.timeStamp = timeStamp;
        this.previousHash = previousHash;
        this.hash = this.calculateHash();
        this.nonce = 0;
    }

    String calculateHash() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            String totalData = this.index + this.data + this.timeStamp + this.previousHash + this.nonce;
            byte[] hash = digest.digest(totalData.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    void mineBlock(int difficulty) {
        String prefixDifficulty = "";
        for (int i = 0; i < difficulty; i++) {
            prefixDifficulty += 0;
        }
        while (!this.hash.substring(0, difficulty).equals(prefixDifficulty)) {
            this.nonce++;
            this.hash = this.calculateHash();
        }

        System.out.println("Block mined: " + this.hash);
    }

}