package nl.tudelft.sem.gateway.router.util;

import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;
import java.util.Set;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

/**
 * ReactiveAuthorizationManager that checks if the IP address of the request is in a given set of
 * address ranges.
 */
public class IpRangeReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final transient Set<IPAddressSeqRange> internalRanges;

    public IpRangeReactiveAuthorizationManager(Set<IPAddressSeqRange> internalRanges) {
        this.internalRanges = internalRanges;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> auth, AuthorizationContext ctx) {
        return Mono.justOrEmpty(ctx.getExchange().getRequest().getRemoteAddress())
                .map((addr) -> new IPAddressString(
                        addr.isUnresolved()
                                ? addr.getHostString()
                                : addr.getAddress().getHostAddress())
                        .getAddress())
                .map((addr) -> internalRanges.stream().anyMatch((range) -> range.contains(addr)))
                .flatMap((matches) -> matches
                        ? ServerWebExchangeMatcher.MatchResult.match()
                        : ServerWebExchangeMatcher.MatchResult.notMatch())
                .switchIfEmpty(ServerWebExchangeMatcher.MatchResult.notMatch())
                .map((matchResult) -> new AuthorizationDecision(matchResult.isMatch()));
    }
}
