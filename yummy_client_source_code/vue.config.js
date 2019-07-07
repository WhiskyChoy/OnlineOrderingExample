module.exports = {
    outputDir: undefined,
    publicPath: undefined,
    assetsDir: undefined,
    runtimeCompiler: undefined,
    productionSourceMap: undefined,
    parallel: undefined,
    css: undefined,
    devServer: {
        proxy: {
            '^/api': {
                target: 'http://127.0.0.1:8080',
                ws: true,
                changeOrigin: true
            },
            '^/resource': {
                target: 'http://127.0.0.1:8080',
                ws: true,
                changeOrigin: true
            }
        }
    }
};