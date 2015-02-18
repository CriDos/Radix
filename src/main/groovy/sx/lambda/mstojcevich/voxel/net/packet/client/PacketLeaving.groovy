package sx.lambda.mstojcevich.voxel.net.packet.client

import groovy.transform.CompileStatic
import io.netty.channel.ChannelHandlerContext
import sx.lambda.mstojcevich.voxel.net.packet.ClientPacket
import sx.lambda.mstojcevich.voxel.net.packet.server.PacketRmEntity
import sx.lambda.mstojcevich.voxel.server.VoxelGameServer
import sx.lambda.mstojcevich.voxel.server.net.ConnectedClient

@CompileStatic
class PacketLeaving implements ClientPacket {

    private final String message

    public PacketLeaving(String message) {
        this.message = message
    }

    @Override
    void handleServerReceive(VoxelGameServer server, ChannelHandlerContext ctx) {
        String hostname = ((InetSocketAddress)ctx.channel().remoteAddress()).hostName
        println "$hostname is leaving. $message."
        if(server.getClient(ctx) != null) {
            if(server.getClient(ctx).player != null) {
                for(ConnectedClient c : server.clientList) {
                    if(c.context != ctx) {
                        println("WE'RE SENDING AN RM")
                        c.context.writeAndFlush(new PacketRmEntity(server.getClient(ctx).player.ID))
                    }
                }
            }
            server.rmClient(ctx)
        }
    }

}
