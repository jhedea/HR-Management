package nl.tudelft.sem.gateway.router.config;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "security")
@ConstructorBinding
public class SecurityConfiguration {
    private final Set<IPAddressSeqRange> internalRanges;

    /**
     * Constructor for SecurityConfiguration.
     *
     * @param internalRanges Internal IP ranges (allow access to internal endpoints)
     */
    public SecurityConfiguration(List<IpRange> internalRanges) {
        this.internalRanges = internalRanges.stream()
                .map(IpRange::toRange)
                .collect(Collectors.toSet());
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class IpRange {
        private final IPAddress start;
        private final IPAddress end;

        public IpRange(String start, String end) {
            this.start = new IPAddressString(start).getAddress();
            this.end = new IPAddressString(end).getAddress();
        }

        public IPAddressSeqRange toRange() {
            return start.spanWithRange(end);
        }
    }
}
