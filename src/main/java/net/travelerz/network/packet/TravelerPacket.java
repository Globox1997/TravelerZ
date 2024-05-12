package net.travelerz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TravelerPacket(int mobLevel) implements CustomPayload {

    public static final CustomPayload.Id<TravelerPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("travelerz", "level_packet"));

    public static final PacketCodec<RegistryByteBuf, TravelerPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.mobLevel);
    }, buf -> new TravelerPacket(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
