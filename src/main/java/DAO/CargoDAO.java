package DAO;

import org.hyperledger.fabric.client.*;
import pojo.Cargo;

import java.nio.file.Path;
import java.util.List;

/**
 * @auther 齿轮
 * @create 2023-03-01-20:25
 */
public interface CargoDAO {
    //初始化方法，给我用户提交的压缩包里的properties文件路径，还有解压完成后目录的路径。
    //使用DAO层方法需要先调用init，并保证没有报错。
    void init(String PATH_CATALOGUE) throws Exception;

    //测试是否可提交事务，失败则抛出异常。
    void initLedger() throws CommitException, GatewayException, InterruptedException;

    //获得世界状态中的全部Cargo类，注意，会很大，你要忍一下！
    List<Cargo> QueryAllCargo() throws GatewayException;

    //注册货物，提供要注册的Cargo类，需要包含：ID、Producer、Name、Introduce、Owner属性，如果已存在则抛出异常；如果出现问题也抛出异常。
    void RegisterCargo(Cargo cargo) throws EndorseException, CommitException, SubmitException, CommitStatusException;

    //更新货物，提供要更新的Cargo类，包含全部属性，没有的属性使用空字符串代替；如果出现问题也抛出异常。
    void UpdateCargo(Cargo cargo) throws EndorseException, CommitException, SubmitException, CommitStatusException;

    //更新Key，提供被更新货物ID和KEY两个字符串；如果出现问题也抛出异常。
    void UpdateKey(String ID, String KEY) throws EndorseException, SubmitException, CommitStatusException;

    //交接验证，提供要验证的货物ID和对方组织的名称，直接对比我方数据库中的KEY和对方数据库中KEY，相同则放回True，否则返回false，如果出现问题也抛出异常。
    boolean SwapVerify(String ID, String Org) throws GatewayException;

    //意义不大的查询，建议使用在更新货物后的刷新步骤。
    Cargo ReadCargo(String ID) throws GatewayException;

    //关闭连接，使用完后记得关闭连接，下一次开启继续调用init
    void close() throws InterruptedException;
}
