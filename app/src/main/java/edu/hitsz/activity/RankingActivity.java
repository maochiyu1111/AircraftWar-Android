package edu.hitsz.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lingber.mycontrol.datagridview.DataGridView;

import java.util.Date;

import edu.hitsz.R;
import edu.hitsz.dao.DataGridRow;
import edu.hitsz.dao.UserDao;
import edu.hitsz.dao.UserDaoImp;

public class RankingActivity extends AppCompatActivity {

    private UserDao userDao;
    private DataGridView<DataGridRow> dataGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // 读取记录
        userDao = UserDaoImp.readFromFile(this, OfflineActivity.gameMode.getFileName());

        dataGridView = findViewById(R.id.data_gridview);
        // 设置列数
        dataGridView.setColunms(4);
        // 设置表头内容
        dataGridView.setHeaderContentByStringId(new int[]{R.string.ranking,R.string.player_name,R.string.score,R.string.record_time});
        // 绑定字段
        dataGridView.setFieldNames(new String[]{"ranking","name","score","time"});
        // 每个column占比
        dataGridView.setColunmWeight(new float[]{1.5f,3,2,3});
        // 每个单元格包含控件
        dataGridView.setCellContentView(new Class[]{TextView.class, TextView.class, TextView.class, TextView.class});
        // 设置数据源
        dataGridView.setDataSource(userDao.toRowList());
        // 单行选中模式
        dataGridView.setSelectedMode(1);
        // 设置可滑动
        dataGridView.setSlidable(true);
        // 设置行高、表头高
        dataGridView.setRowHeight(200);
        dataGridView.setHeaderHeight(100);
        // 初始化表格
        dataGridView.initDataGridView();
        // 注册点击监听器，点击时删除行
        dataGridView.setOnItemCellClickListener((v, row, column) -> delRow(row));

        inputName();
    }

    private void inputName() {
        EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("请输入名字以记录得分")
                .setView(input)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            String name = input.getText().toString();
            if (!name.isEmpty()) {
                userDao.add(name,getIntent().getIntExtra("score",0),new Date());
                userDao.getSorted();
                userDao.writeToFile(this,OfflineActivity.gameMode.getFileName());
                dataGridView.setDataSource(userDao.toRowList());
                dataGridView.updateAll();
            } else {
                Toast.makeText(this, "签名为空", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void delRow(int position) {
        int rank = position + 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("是否确认删除第" + rank +"名？")
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            userDao.deleteById(userDao.getByIndex(position).getId());
            userDao.writeToFile(this,OfflineActivity.gameMode.getFileName());
            dataGridView.setDataSource(userDao.toRowList());
            dataGridView.updateAll();
        });
        builder.show();
    }

}
