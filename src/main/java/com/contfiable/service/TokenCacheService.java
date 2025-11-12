package com.contfiable.service;

import com.contfiable.dto.user.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {

	private static final Logger logger = LoggerFactory.getLogger(TokenCacheService.class);

	private static final long CACHE_TTL_MINUTES = 5;

	private final ConcurrentHashMap<String, CacheEntry> tokenCache = new ConcurrentHashMap<>();

	private static class CacheEntry {
		private final AuthResponse authResponse;
		private final long timestamp;

		public CacheEntry(AuthResponse authResponse) {
			this.authResponse = authResponse;
			this.timestamp = System.currentTimeMillis();
		}

		public boolean isExpired() {
			return System.currentTimeMillis() - timestamp > TimeUnit.MINUTES.toMillis(CACHE_TTL_MINUTES);
		}

		public AuthResponse getAuthResponse() {
			return authResponse;
		}
	}

	public AuthResponse getCachedAuthResponse(String token) {
		CacheEntry entry = tokenCache.get(token);
		if (entry == null) {
			return null;
		}

		if (entry.isExpired()) {
			tokenCache.remove(token);
			logger.debug("Token cache expired for token: {}", token.substring(0, Math.min(10, token.length())) + "...");
			return null;
		}

		logger.debug("Token cache hit for token: {}", token.substring(0, Math.min(10, token.length())) + "...");
		return entry.getAuthResponse();
	}

	public void cacheAuthResponse(String token, AuthResponse authResponse) {
		if (authResponse != null) {
			tokenCache.put(token, new CacheEntry(authResponse));
			logger.debug("Token cached for token: {}", token.substring(0, Math.min(10, token.length())) + "...");
		}
	}

	public void cleanExpiredTokens() {
		tokenCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
		logger.debug("Cleaned expired tokens from cache. Current cache size: {}", tokenCache.size());
	}

	@Scheduled(fixedDelay = 60_000)
	public void scheduledClean() {
		cleanExpiredTokens();
	}

	public void clearCache() {
		tokenCache.clear();
		logger.debug("Token cache cleared");
	}
}


