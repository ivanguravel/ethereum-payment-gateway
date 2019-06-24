package com.crypto.services;

import com.crypto.blockchain.Web3jProvider;
import com.crypto.dao.UserRepository;
import com.crypto.dto.CreateWalletResponse;
import com.crypto.dto.TransactionDto;
import com.crypto.dto.TransactionResponse;
import com.crypto.dto.WalletBalanceResponse;
import com.crypto.model.User;
import com.crypto.utils.PasswordUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import static com.crypto.utils.BlockchainUtils.buildCredentials;
import static com.crypto.utils.BlockchainUtils.createECKeyPair;
import static com.crypto.utils.BlockchainUtils.hexPrefix;
import static org.web3j.tx.Transfer.sendFunds;
import static org.web3j.utils.Convert.Unit.ETHER;

@Service
@Transactional
@Slf4j
public class EthereumService {
    private final UserRepository userRepository;

    private final Web3jProvider web3jProvider;

    @Autowired
    public EthereumService(UserRepository userRepository, Web3jProvider web3jProvider) {
        this.userRepository = userRepository;
        this.web3jProvider = web3jProvider;
    }

    public CreateWalletResponse create(String email) throws Exception {
        String password = PasswordUtils.generatePassword();
        ECKeyPair keyPair = createECKeyPair();
        WalletFile wallet = Wallet.createStandard(password, keyPair);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setWalletAddress(hexPrefix(wallet.getAddress()));
        user.setPrivateKey(keyPair.getPrivateKey().toString());
        user.setPublicKey(keyPair.getPublicKey().toString());

        userRepository.save(user);

        return new CreateWalletResponse(user.getEmail(), user.getWalletAddress());
    }

    public WalletBalanceResponse balanceOf(String email) throws Exception {
        return new WalletBalanceResponse(email,
                getEtherBalance(userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Wallet not found for email: " + email))
                        .getWalletAddress()));
    }

    public TransactionResponse send(TransactionDto dto) throws Exception {
        User from = userRepository.findByWalletAddress(dto.getFrom()).orElseThrow(() -> new RuntimeException("Wallet not found: " + dto.getFrom()));

        try {
            TransactionReceipt transactionReceipt = sendFunds(web3jProvider.get(),
                        buildCredentials(from.getPrivateKey(), from.getPublicKey()),
                        dto.getTo(),
                        BigDecimal.valueOf(dto.getAmount()), ETHER)
                    .send();
            log.info("ETH transfer success. Sender: {}, Receiver: {}, Amount: {}", dto.getFrom(), dto.getTo(), dto.getAmount());
            return new TransactionResponse(transactionReceipt.getTransactionHash());
        } catch (Exception e) {
            log.error("Error sending ETH to user. Sender: {}, Receiver: {}, Amount: {}", dto.getFrom(), dto.getTo(), dto.getAmount());
            throw new RuntimeException(e);
        }

    }

    private BigDecimal getEtherBalance(String address) {
        try {
            EthGetBalance ethGetBalance = web3jProvider.get()
                    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .send();
            return Convert.fromWei(ethGetBalance.getBalance().toString(), ETHER);
        } catch (Exception e) {
            log.error("Failed to retrieve user's ETH balance. Exception: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
