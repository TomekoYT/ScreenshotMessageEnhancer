package tomeko.screenshotmessageenhancer.compat

//? if 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
*///?} else {
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
//?}

fun getStyledChatMessage(
    message: String,
    //? if 1.8.9 {
    //color: EnumChatFormatting
    //?} else {
    color: ChatFormatting
    //?}
):
//? if 1.8.9 {
        //IChatComponent
//?} else {
Component
//?}
{
    //? if 1.8.9 {
    //return ChatComponentText(message).setChatStyle(ChatStyle().setColor(color))
    //?} else {
    return Component.literal(message).withStyle { style -> style.withColor(color) }
    //?}
}

fun sendChatMessage(
    //? if 1.8.9 {
    //message: IChatComponent,
    //?} else {
    message: Component,
    //?}
    showMessage: Boolean
) {
    val mc =
        //? if 1.8.9 {
        //Minecraft.getMinecraft()
    //?} else {
    Minecraft.getInstance()
    //?}
    if (!showMessage ||
        //? if 1.8.9 {
        //mc.thePlayer == null
    //?} else {
    mc.player == null
    //?}
    ) return

    //? if 1.8.9 {
    //mc.thePlayer.addChatMessage(message)
    //?} elif >= 26.2 {
    //mc.gui.hud.chat.addClientSystemMessage(message)
    //?} elif >= 26.1 {
    mc.gui.chat.addClientSystemMessage(message)
    //?} else {
    //mc.gui.chat.addMessage(message)
    //?}
}