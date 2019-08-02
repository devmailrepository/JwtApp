package com.jwtapp.service;

import com.jwtapp.entity.Account;
import com.jwtapp.exception.ClientError;
import com.jwtapp.exception.ParameterMissingException;
import com.jwtapp.repository.AccountRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(String username, String email, String password) {
        final boolean isExists = accountRepository.exists(username, email);
        if (isExists)
            throw new ClientError("User with such username or email already exists");
        Account account = new Account(username, email, passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public void generateJwt(String authToken) throws JOSEException, NoSuchProviderException, NoSuchAlgorithmException {
        String preDecodeString = authToken.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(preDecodeString);
        String decodeStr = new String(decodedBytes);
        String[] stringArr = decodeStr.split(":");
        if (stringArr.length != 2) {
            throw new ParameterMissingException("username or password is missing");
        }
        final String usernameVerify = stringArr[0];
        final String passwordVerify = stringArr[1];
        final Account accountFromDB = accountRepository.getAccountByName(usernameVerify);
//        System.out.println(accountFromDB.toString());

        if (!BCrypt.checkpw(passwordVerify, accountFromDB.getPassword())) {
            throw new ParameterMissingException("Password is not correct");
        }
        if (!accountFromDB.isEnable())
            throw new ParameterMissingException("Sorry but your account is disable");

//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(2048);
//        KeyPair keyPair = keyGen.generateKeyPair();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//
//
//        JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
//        EncryptedJWT jwt = new EncryptedJWT(header, jwtClaims);
//        RSAEncrypter encrypter = new RSAEncrypter((RSAPublicKey) publicKey);
//        jwt.encrypt(encrypter);
//        String jwtString = jwt.serialize();
//        System.out.println(jwtString);

        RSAKey rsaKey = new RSAKeyGenerator(2048)
            .keyID("MaxPain")
            .generate();
        JWSSigner signer = new RSASSASigner(rsaKey);

        LocalDateTime ldt = LocalDateTime.now().plusSeconds(60);
        Date expire = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("!!!!!!" + expire.toString());
        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
            .issuer("jwtapp")
            .subject(accountFromDB.getRoles().toString())
            .expirationTime(expire)
            .build();
        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.getKeyID())
                .build(),
            jwtClaims);
        signedJWT.sign(signer);
        String jwtString = signedJWT.serialize();
        System.out.println(jwtString);
//        System.out.println(jwtString.length()*2 +"Byte");

    }


}




