import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

// 主程式類，包含音樂撥放GUI和撥放列表管理
public class MusicPlayer {
    private JFrame frame; // 主視窗
    private JPanel panel; // 主面板
    private JTextField titleField; // 輸入歌曲標題的文字框
    private JTextField artistField; // 輸入歌手名稱的文字框
    private JTextField durationField; // 輸入歌曲播放秒數的文字框
    private JButton addButton; // 新增歌曲按鈕
    private JButton deleteButton; // 刪除歌曲按鈕
    private JButton playNextButton; // 播放下一首按鈕
    private JButton playRandomButton; // 隨機播放按鈕
    private JList<Music> playlist; // 顯示撥放列表的 JList
    private DefaultListModel<Music> listModel; // JList 的資料模型
    private LinkedList<Music> musicList; // 管理音樂撥放列表的 LinkedList
    private Timer playTimer; // 播放計時器

    // 主程式的建構子
    public MusicPlayer() {
        frame = new JFrame("Music Player"); // 創建新的 JFrame 並設置標題
        panel = new JPanel(); // 創建新的 JPanel
        panel.setLayout(new FlowLayout()); // 使用流式佈局

        titleField = new JTextField(15); // 創建標題輸入文字框，寬度為 15
        artistField = new JTextField(15); // 創建歌手名稱輸入文字框，寬度為 15
        durationField = new JTextField(5); // 創建歌曲播放秒數輸入文字框，寬度為 5
        addButton = new JButton("Add"); // 創建新增歌曲按鈕，按鈕文字為 "Add"
        deleteButton = new JButton("Delete"); // 創建刪除歌曲按鈕，按鈕文字為 "Delete"
        playNextButton = new JButton("Play Next"); // 創建播放下一首按鈕，按鈕文字為 "Play Next"
        playRandomButton = new JButton("Play Random"); // 創建隨機播放按鈕，按鈕文字為 "Play Random"
        listModel = new DefaultListModel<>(); // 創建 JList 的資料模型
        playlist = new JList<>(listModel); // 創建顯示撥放列表的 JList
        JScrollPane scrollPane = new JScrollPane(playlist); // 創建捲動視窗，用於 JList

        // 將各個元件添加到主面板上
        panel.add(new JLabel("Title:")); // 添加標題標籤
        panel.add(titleField); // 添加標題輸入文字框
        panel.add(new JLabel("Artist:")); // 添加歌手名稱標籤
        panel.add(artistField); // 添加歌手名稱輸入文字框
        panel.add(new JLabel("Duration (sec):")); // 添加歌曲播放秒數標籤
        panel.add(durationField); // 添加歌曲播放秒數輸入文字框
        panel.add(addButton); // 添加新增歌曲按鈕
        panel.add(deleteButton); // 添加刪除歌曲按鈕
        panel.add(playNextButton); // 添加播放下一首按鈕
        panel.add(playRandomButton); // 添加隨機播放按鈕
        panel.add(scrollPane); // 添加捲動視窗，顯示撥放列表的 JList

        frame.add(panel); // 將主面板添加到主視窗中
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設置關閉操作：點擊 X 退出程式
        frame.pack(); // 自動設置視窗大小
        frame.setVisible(true); // 設置主視窗可見

        musicList = new LinkedList<>(); // 初始化音樂撥放列表

        // 新增歌曲按鈕的事件監聽器
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim(); // 獲取輸入的標題，去除前後空格
                String artist = artistField.getText().trim(); // 獲取輸入的歌手名稱，去除前後空格
                String durationText = durationField.getText().trim(); // 獲取輸入的歌曲播放秒數，去除前後空格
                int duration = 0;
                try {
                    duration = Integer.parseInt(durationText); // 將歌曲播放秒數轉換為整數
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid duration! Please enter a valid number."); // 若輸入不是數字，顯示錯誤訊息
                    return;
                }
                if (!title.isEmpty() && !artist.isEmpty() && duration > 0) { // 確保標題、歌手名稱不為空且播放秒數大於 0
                    Music music = new Music(title, artist, duration); // 創建新的音樂物件
                    musicList.addLast(music); // 將音樂添加到撥放列表的末尾
                    listModel.addElement(music); // 將音樂添加到 JList 的資料模型中
                    updateList(); // 更新撥放列表的顯示
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields and ensure duration is greater than 0."); // 提示用戶填寫完整信息
                }
            }
        });

        // 刪除歌曲按鈕的事件監聽器
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = playlist.getSelectedIndex(); // 獲取被選中的項目的索引
                if (selectedIndex != -1) { // 確保有選中的項目
                    musicList.remove(selectedIndex); // 從撥放列表中刪除選中的音樂
                    listModel.remove(selectedIndex); // 從 JList 的資料模型中刪除對應的項目
                    updateList(); // 更新撥放列表的顯示
                }
            }
        });

        // 播放下一首按鈕的事件監聽器
        playNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playNextMusic(); // 播放下一首音樂
            }
        });

        // 隨機播放按鈕的事件監聽器
        playRandomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playRandomMusic(); // 隨機播放音樂
            }
        });

        // 設定播放計時器，每秒檢查是否需要換下一首歌曲
        playTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!musicList.isEmpty()) { // 確保撥放列表不為空
                    Music currentMusic = musicList.getFirst(); // 獲取當前播放的音樂
                    currentMusic.reduceDuration(); // 減少當前音樂的播放秒數
                    if (currentMusic.getDuration() <= 0) { // 若當前音樂播放完畢
                        musicList.removeFirst(); // 從撥放列表中移除當前音樂
                        listModel.remove(0); // 從 JList 的資料模型中移除對應的項目
                        updateList(); // 更新撥放列表的顯示
                        playNextMusic(); // 播放下一首音樂
                    }
                }
            }
        });
        playTimer.setInitialDelay(0); // 設置計時器初始延遲為 0
    }

    // 播放下一首音樂
    private void playNextMusic() {
        if (!musicList.isEmpty()) { // 確保撥放列表不為空
            Music nextMusic = musicList.getFirst(); // 獲取撥放列表中的第一首音樂
            JOptionPane.showMessageDialog(frame, "Now playing:\n" + nextMusic); // 以對話框顯示目前撥放的音樂
            playTimer.restart(); // 啟動播放計時器
        } else {
            JOptionPane.showMessageDialog(frame, "Playlist is empty!"); // 若撥放列表為空，顯示訊息框提示
        }
    }

    // 隨機播放音樂
    private void playRandomMusic() {
        if (!musicList.isEmpty()) { // 確保撥放列表不為空
            Random random = new Random();
            int randomIndex = random.nextInt(musicList.size()); // 隨機生成一個索引
            Music randomMusic = musicList.get(randomIndex); // 獲取隨機選中的音樂
            JOptionPane.showMessageDialog(frame, "Now playing (random):\n" + randomMusic); // 以對話框顯示隨機播放的音樂
            musicList.remove(randomIndex); // 從撥放列表中移除隨機播放的音樂
            listModel.remove(randomIndex); // 從 JList 的資料模型中移除對應的項目
            updateList(); // 更新撥放列表的顯示
            playTimer.restart(); // 啟動播放計時器
        } else {
            JOptionPane.showMessageDialog(frame, "Playlist is empty!"); // 若撥放列表為空，顯示訊息框提示
        }
    }

    // 更新撥放列表的 JList
    private void updateList() {
        playlist.clearSelection(); // 清除 JList 中的選擇狀態
    }

    // 主程式的入口點，創建並啟動音樂撥放程式
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MusicPlayer(); // 創建並顯示音樂撥放程式的 GUI
            }
        });
    }

    // 音樂類別
    private static class Music {
        private String title; // 歌曲標題
        private String artist; // 歌手
        private int duration; // 歌曲播放秒數
        private Timer timer; // 計時器

        // 音樂建構子
        public Music(String title, String artist, int duration) {
            this.title = title;
            this.artist = artist;
            this.duration = duration;

            // 使用 final 局部變數以確保在 ActionListener 內可訪問
            final int initialDuration = duration;

            this.timer = new Timer(1000, new ActionListener() {
                int remainingDuration = initialDuration; // 使用 final 的局部變數

                @Override
                public void actionPerformed(ActionEvent e) {
                    remainingDuration--; // 減少播放秒數
                    if (remainingDuration <= 0) {
                        timer.stop(); // 若播放結束，停止計時器
                    }
                }
            });
        }

        // 取得歌曲標題
        public String getTitle() {
            return title;
        }

        // 取得歌手名稱
        public String getArtist() {
            return artist;
        }

        // 取得歌曲播放秒數
        public int getDuration() {
            return duration;
        }

        // 覆寫 toString 方法，用於 JList 的顯示
        @Override
        public String toString() {
            return title + " - " + artist;
        }

        // 開始播放音樂
        public void startPlaying() {
            timer.start(); // 啟動計時器
        }

        // 減少歌曲播放秒數
        public void reduceDuration() {
            duration--; // 減少播放秒數
            if (duration <= 0) {
                timer.stop(); // 若播放結束，停止計時器
            }
        }
    }
}