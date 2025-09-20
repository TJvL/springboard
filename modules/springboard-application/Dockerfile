# Production-only Dockerfile for GraalVM native binary
# Stage 1: Musl toolchain and zlib build - separate layer for better caching
FROM alpine:3.22.1 AS musl-toolchain

# Install dependencies for musl toolchain
RUN apk add --no-cache wget tar gzip make gcc

# Download and install musl toolchain (using version 10 for better static linking compatibility)
ENV TOOLCHAIN_DIR=/opt/musl

RUN wget --progress=bar:force:noscroll http://more.musl.cc/10.2.1/x86_64-linux-musl/x86_64-linux-musl-native.tgz
RUN tar -xzf x86_64-linux-musl-native.tgz -C /opt
RUN mv /opt/x86_64-linux-musl-native ${TOOLCHAIN_DIR}
RUN rm x86_64-linux-musl-native.tgz

# Download, compile and install zlib with musl
ENV CC=${TOOLCHAIN_DIR}/bin/gcc

RUN wget --progress=bar:force:noscroll https://zlib.net/zlib-1.3.1.tar.gz
RUN tar -xzf zlib-1.3.1.tar.gz

WORKDIR /zlib-1.3.1
RUN ./configure --prefix=${TOOLCHAIN_DIR} --static
RUN make -j$(nproc)
RUN make install

WORKDIR /
RUN rm -rf zlib-1.3.1 zlib-1.3.1.tar.gz

# Stage 2: Application build stage with pre-built musl toolchain
FROM ghcr.io/graalvm/graalvm-community:21.0.2 AS builder

# Copy pre-built musl toolchain from previous stage
COPY --from=musl-toolchain /opt/musl /opt/musl

# Set up toolchain environment
ENV TOOLCHAIN_DIR=/opt/musl
ENV PATH="${TOOLCHAIN_DIR}/bin:${PATH}"

# Set up working directory
WORKDIR /build

# Copy only what's needed for the build
COPY gradle/ gradle/
COPY gradlew gradle.properties settings.gradle.kts build.gradle.kts ./
COPY gradle/libs.versions.toml gradle/

# Copy module build files
COPY modules/springboard-core/build.gradle.kts modules/springboard-core/
COPY modules/springboard-expression/build.gradle.kts modules/springboard-expression/
COPY modules/springboard-inference/build.gradle.kts modules/springboard-inference/
COPY modules/springboard-expression-web/build.gradle.kts modules/springboard-expression-web/
COPY modules/springboard-inference-web/build.gradle.kts modules/springboard-inference-web/
COPY modules/springboard-application/build.gradle.kts modules/springboard-application/

# Download dependencies first for caching
RUN ./gradlew dependencies --no-daemon --info

# Copy source code
COPY modules/springboard-core/src/ modules/springboard-core/src/
COPY modules/springboard-expression/src/ modules/springboard-expression/src/
COPY modules/springboard-inference/src/ modules/springboard-inference/src/
COPY modules/springboard-expression-web/src/ modules/springboard-expression-web/src/
COPY modules/springboard-inference-web/src/ modules/springboard-inference-web/src/
COPY modules/springboard-application/src/ modules/springboard-application/src/

# Build native image (disable configuration cache due to GraalVM plugin compatibility issues)
RUN ./gradlew :modules:springboard-application:nativeCompile --no-daemon --no-configuration-cache --info --stacktrace

# Stage 2: Fully static binary - no runtime dependencies needed
FROM scratch

# Copy fully static native executable (120MB musl-linked binary)
COPY --from=builder /build/modules/springboard-application/build/native/nativeCompile/springboard-application /app

# Expose port
EXPOSE 8080

# Set production profile
ENV SPRING_PROFILES_ACTIVE=production

# Run fully static native binary
ENTRYPOINT ["/app"]
