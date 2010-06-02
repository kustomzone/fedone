package wave.util.wavesig;

import java.util.Arrays;
import java.util.HashMap;

import org.rice.crosby.batchsig.Message;
import org.rice.crosby.batchsig.Signer;
import org.rice.crosby.historytree.MerkleTree;
import org.rice.crosby.historytree.aggs.SHA256Agg;
import org.rice.crosby.historytree.generated.Serialization.PrunedTree;
import org.rice.crosby.historytree.generated.Serialization.SigConfig;
import org.rice.crosby.historytree.generated.Serialization.SigTreeType;
import org.rice.crosby.historytree.generated.Serialization.TreeSigBlob;
import org.rice.crosby.historytree.generated.Serialization.TreeSigMessage;
import org.rice.crosby.historytree.generated.Serialization.TreeType;
import org.rice.crosby.historytree.storage.HashStore;

import com.google.protobuf.ByteString;

public class Verifier {
	private Signer signer;
	final SigConfig sigconfig;

	public Verifier(Signer signer) {
		this.signer=signer;
		sigconfig = SigConfig.newBuilder().setTreetype(SigTreeType.MERKLE_TREE).build();
	}
	
	boolean verify(Message message) {
		TreeSigBlob sigblob = message.getSigBlob();
		
		// Other choices are unsupported in this code.
		if (sigblob.getConfig().getTreetype() != TreeType.SINGLE_MERKLE_TREE)
			return false;
		
		// Parse the tree.
		MerkleTree<byte[],byte[]> parsed=parseTree(sigblob.getTree());
		final byte[] rootHash = parsed.agg();

		// See if the message is in the tree.
		byte [] leafagg = parsed.leaf(sigblob.getLeaf()).getAgg();
		byte [] msgagg = parsed.getAggObj().aggVal(message.getData());
		
		// FIXME! THIS WILL THROW AN ERROR THATS NOT BEING CAUGHT ON INVALID PROOF.
		if (! Arrays.equals(msgagg,leafagg))
			// Nope, we fail.
			return false;
		
		TreeSigMessage.Builder msgbuilder = TreeSigMessage.newBuilder();
		msgbuilder.setConfig(sigconfig);
		msgbuilder.setVersion(parsed.version());
		msgbuilder.setRoothash(ByteString.copyFrom(rootHash));

		byte[] signeddata = msgbuilder.build().toByteArray();
		byte[] sig = sigblob.getSig().toByteArray();

		if (checkCache(signeddata,sig)) {
			return true;
		}

		// Signature doesn't verify.
		if (! signer.verify(signeddata, sig)) {
			return false;
		}
		
		// All good.
		addCache(signeddata,sig);
		return true;
	}

	public MerkleTree<byte[],byte[]> parseTree(PrunedTree pb) {
		MerkleTree<byte[],byte[]> tree= new MerkleTree<byte[],byte[]>(new SHA256Agg(),new HashStore<byte[],byte[]>());
		tree.updateTime(pb.getVersion());
		tree.parseTree(pb);
		return tree;
	}	

	/** Indicate that the given signature signing the given data has been verified as true */
	private void addCache(byte[] signeddata, byte[] sig) {
	}

	/** Has the given data signed by the given signature been verified as valid? */
	boolean checkCache(byte[] data, byte[] signature) {
		return false; // TODO: (SUGGESTIONS) Implement a cache.
	}
}
