package com.nazmul_anik.login_with_jwt.logout;

import java.util.HashSet;
import java.util.Set;

public class BlackList {
    public Set<String> blackListTokenSet = new HashSet<>();

    public void blackListToken(String token){
        blackListTokenSet.add(token);
    }
    public boolean isBlackListed(String token){
        return blackListTokenSet.contains(token);
    }
}
