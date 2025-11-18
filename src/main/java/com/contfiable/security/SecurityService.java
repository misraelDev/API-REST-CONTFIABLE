package com.contfiable.security;

import com.contfiable.exception.BadRequestException;
import com.contfiable.model.User;
import com.contfiable.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene el usuario autenticado desde el SecurityContext
     * @return Usuario autenticado
     * @throws BadRequestException si no hay usuario autenticado
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityService.class);
        
        if (authentication == null) {
            logger.error("Authentication es null");
            throw new BadRequestException("No hay usuario autenticado");
        }
        
        if (!authentication.isAuthenticated()) {
            logger.error("Authentication no está autenticado. Principal: {}", authentication.getPrincipal());
            throw new BadRequestException("No hay usuario autenticado");
        }

        Object principal = authentication.getPrincipal();
        logger.info("Principal type: {}, Principal: {}", principal.getClass().getName(), principal);
        
        String identifier;
        
        if (principal instanceof UserDetails userDetails) {
            identifier = userDetails.getUsername();
            logger.info("Identifier desde UserDetails: {}", identifier);
        } else if (principal instanceof String) {
            identifier = (String) principal;
            logger.info("Identifier desde String: {}", identifier);
        } else {
            logger.error("Principal no es UserDetails ni String. Type: {}", principal.getClass().getName());
            throw new BadRequestException("No se pudo obtener el identificador del usuario autenticado");
        }

        // Intentar primero como ID numérico, si falla intentar como email
        try {
            Long userId = Long.parseLong(identifier);
            logger.info("Buscando usuario por ID: {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("Usuario autenticado no encontrado con ID: " + userId));
            logger.info("Usuario encontrado: {}", user.getEmail());
            return user;
        } catch (NumberFormatException e) {
            // Si no es un número, intentar buscar por email
            logger.info("Identifier no es numérico, buscando por email: {}", identifier);
            User user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new BadRequestException("Usuario autenticado no encontrado con email: " + identifier));
            logger.info("Usuario encontrado por email: {}", user.getEmail());
            return user;
        }
    }

    /**
     * Obtiene el ID del usuario autenticado
     * @return ID del usuario autenticado
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}

