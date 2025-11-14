import Config from "./Config";

export default class Icon {
    static ICON_ROOT = Config.ROOT_URL + "/download";

    static AVEREON_DARK = this.ICON_ROOT + "/stable/avereon/provider/icon?theme=dark";
    static AVEREON_LIGHT = this.ICON_ROOT + "/stable/avereon/provider/icon?theme=light";
    static AVEREON = this.AVEREON_LIGHT;

    // Products
    static ACORN = this.ICON_ROOT + "/latest/acorn/product/icon";

    static SEENC = this.AVEREON_LIGHT;

    static WEAVE = this.ICON_ROOT + "/latest/weave/product/icon";

    static XENON_DARK = this.ICON_ROOT + "/latest/xenon/product/icon?theme=dark";
    static XENON_LIGHT = this.ICON_ROOT + "/latest/xenon/product/icon?theme=light";
    static XENON = this.XENON_LIGHT;

    // Libraries
    static CURVE = this.AVEREON_LIGHT;
    static ZARRA = this.AVEREON_LIGHT;
    static ZAVRA = this.AVEREON_LIGHT;
    static ZENNA = this.AVEREON_LIGHT;
    static ZERRA = this.AVEREON_LIGHT;
    static ZEVRA = this.AVEREON_LIGHT;

    // Maven Plugins
    static CAMEO = this.AVEREON_LIGHT;
    static CUREX = this.AVEREON_LIGHT;

    // Xenon Modules
    //static ACORN= iconRoot + "/latest/acorn/product/icon";
    static AMAZO = this.ICON_ROOT + "/latest/amazo/product/icon";
    static ARENA = this.ICON_ROOT + "/latest/arena/product/icon";
    static AVEON = this.ICON_ROOT + "/latest/aveon/product/icon";
    static CARTA = this.ICON_ROOT + "/latest/carta/product/icon";
    static MAZER = this.ICON_ROOT + "/latest/mazer/product/icon";
    static RECON = this.ICON_ROOT + "/latest/recon/product/icon";
}
