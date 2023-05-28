package me.reb4ck.smp.base.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableMap;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.api.utils.message.SpecialMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Messages {
    public String noPermission = "&cYou don't have permission to do that!";
    public String unknownCommand = "&cUnknown command!";
    public String mustBeAPlayer = "&cYou must be a player to do that!";
    public String invalidArguments = "&7Invalid Arguments!";
    public String invalidKey = "&7Invalid Key!";
    public String specialCharactersNotAllowed = "&cSpecials Characters Not allowed!";
    public String successfullyReloaded = "&aSuccessfully reloaded config files!";

    /*
    Help Messages
     */
    public String activeCooldown = "%prefix% &7You cannot do that due to a running cooldown, please wait %hours% hour(s), %minutes% minute(s), %seconds% second(s)!";

    public String helpMessage = "&7%usage% - &e%description%";
    public String helpHeader = "      &6&lSMP   ";
    public String helpfooter = "&e<< &6Page %page% of %max_page% &e>>";
    public String previousPage = "<<";
    public String nextPage = ">>";
    public String helpPageHoverMessage = "Click to go to page %page%";

    public String serverIsOn = "&cServer Is Already Online";
    public String serverIsOff = "&cServer Is Already Offline";
    public String youDontHaveServerWithThatName = "&cYou don't have a server named %server%";
    public String serverNamedDoesntExist = "&cThere isn't a server named %server%";
    public String exceedDisplayNameLimit = "&cThat display name contains too many characters!";
    public String exceedNameLimit = "&cThat name contains too many characters!";
    public String exceedDescriptionLineLimit = "&cThat description line contains too many characters!";
    public String exceedDescriptionLimit = "&cThe limit of description lines has been reached!";
    public String successfullyOped = "&aYou have been successfully opped in %server% server!";

    public String glowMessage = "&7Glow message can be &a'true' or &c'false'&7!";

    public String serverOffLineStarting = "&aServer Offline, Starting server before teleport!";
    public String cantTeleportIsOffline = "&cYou can't teleport to that server is currently offline!";

    public String youDontHaveServer = "&cYou don't have any server!";

    public String teleporting = "&aTeleporting...";

    public String youDontHaveEmptySlots = "&cYou don't have more slots to add server!";
    public String nameInUse = "&cA Server with that name already exists!";
    public String successfullyCreated = "&aYou have created successfully a server named %server%, all files are being prepared!";
    public String successfullyDeleted = "&aYou have successfully deleted your server %server%!";
    public String deletingServer = "&aDeleting Server...";
    public String serverLimitReach = "&cThat server can't start, server reach limit of available servers online!";

    public String errorDeleting = "&cError deleting &b%server%&c!";
    public String successfullyDeletedAll = "&aSuccessfully deleted &2%amount% &aSMP Servers!";

    public String noPortsAvailable = "&cNo port available trying to create a server!";

    public String successfullyRemovedLine = "&aYou have successfully removed last description line!";
    public String successfullyResetDescription = "&aYou have successfully reset your server description!";

    public String descriptionIsEmpty = "&cDescription is empty!";
    public String successfullyUpdatedDisplayName = "&aYou have successfully updated your server displayname!";
    public String successfullyUpdatedDescription = "&aYou have successfully updated your server description!";
    public String successfullyUpdatedLogo = "&aYou have successfully updated your server logo!";
    public String successfullyStopped = "&aYou have successfully stopped your server %server%!";
    public String successfullyStarted = "&aYou have successfully started your server %server%!";

    public List<String> defaultDescription = Arrays.asList("", "&c&lANARCHY SERVER!", "&bJoin for fun", "");

    public List<String> ftpData = Arrays.asList("&2» &aFTP Data: " ,"&2» &7User name: &a%username%");
    public SpecialMessage ftpPassword = new SpecialMessage(Collections.emptyList(), "&2» &7Password: &a&k%password%", "&aClick to copy password!");

    public String moreFavoritesAreNotAllowed = "&cMore favorites are not allowed!";
    public String youDontHavePermissionsToAddAFavoriteServer = "&cYou don't have more slots to add this server to your favorites!";
    public String favoriteServerAdded = "&aYou successfully added %server% to your favorite list!";
    public String alreadyFavorite = "&cThis server is already in your favorite list!";
    public String noMoreSpaceToFeature = "&cThere's not more space to feature a server!";
    public String successfullyAddedToFeature = "&aYou successfully payed %feature_price% your server %server_name% to feature list for %time_format%!";
    public String notEnoughMoneyToFeature = "&cYou don't have enough money to feature your server!";
    public String unexpectedErrorFeaturing = "&cThere was an unexpected error featuring server!";

    public String timeFormat = "&b%days%%hours%%minutes%%seconds%";
    public String noFeatured = "&cNo Featured.";

    public String days = "%days%d ";
    public String hours = "%hours%h ";
    public String minutes = "%minutes%m ";
    public String seconds = "%seconds%s ";

    public List<String> adminMessage = Arrays.asList("&2» &aWelcome to your server %server_name% &2«", "&2» &7Use /smp admin to manage your server");

    public Map<Boolean, String> serverStatusPlaceholder = ImmutableMap.<Boolean, String>builder()
            .put(true, "&aOnline")
            .put(false, "&cOffline")
            .build();

    public String alreadyUpgraded = "&cAlready Upgrade!";
    public String clickToUpgrade = "&eClick to upgrade!";

    public String successfullyUpdatedRam = "&7You successfully bought a ram upgrade for a price of &6%price% &2» &c%old_server_ram%&8&l➜&a%server_ram% &a&lRESTART YOUR SERVER TO VIEW CHANGES!";

    public String notEnoughMoneyToRam = "&cYou dont have enough money to buy a server upgrade!";
}
