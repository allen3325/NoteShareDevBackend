package ntou.notesharedevbackend.noteModule.service;

import ntou.notesharedevbackend.noteModule.PlagiarismChecker;
import ntou.notesharedevbackend.noteModule.entity.Article;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.Plagiarismdictionary;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PlagiarismDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PlagiarismService {

    @Autowired
    @Lazy
    private NoteRepository noteRepository;
    @Autowired
    @Lazy
    private PlagiarismDictionaryRepository plagiarismDictionaryRepository;

//    private static PrintWriter output; // outputs text to a file
//    private static File dictionary = new File("dictionary.txt");

    public HashMap<String, Float> plagiarismPointChecker(ArrayList<String> noteIDArray, String self) {
        float maxPoint = 0;
        float maxTotalTextEqual = 0;
        float maxLls = 0;
        HashMap<String, Float> result = new HashMap<>();
        result.put("tiger", 0F);
        // TODO: 內容在 mycustom_html，記得清掉 html tag -> 先拿 noteIDArray（存ID） 之內容
        PlagiarismChecker plagiarismChecker = new PlagiarismChecker();
        final String containsEngRegex = " *[a-zA-Z]+ *";
        int sentenceLengthParam; // 決定lls可以進入字典的長度參數（目前以一句話為主）
        // 1. 比較多個(tag過濾第二層)
        for (String noteID : noteIDArray) {
            Article article = new Article(getHtmlCodeInNote(noteID));
            if (!article.getArticle().equals("")) {
                String comparison = article.getArticle();
                article = null;
//            System.out.println("------------- new comparison is : "+comparison.replaceAll(System.lineSeparator(),"")+"-------------");
                // 抓到一模一樣，直接 return
                if (self.equals(comparison)) {
                    result.put("point", 100F);
                    result.put("totalTextEqual", (float) self.length());
                    result.put("lls", (float) self.length());
                    return result;
                }
                // 1-1. 先比對抄襲指數並且得到lls陣列
                // TODO new : trivial判斷 (目前先刪掉)
                ArrayList<String> llsArray = plagiarismChecker.getLlsArray(comparison, self);
                int lengthToDivide = plagiarismChecker.getArticleLengthDivide(comparison, self);
                float totalTextEqual = plagiarismChecker.getTotalTextEqual(comparison, self);
                float plagiarismPoint = plagiarismChecker.getPlagiarism(totalTextEqual, lengthToDivide);
//            float plagiarismPoint;
//            System.out.println("###################################################################");
//            System.out.println("文章共有" + self.length() + "個字且有" + totalTextEqual + "個字重複。");
//            System.out.println("plagiarismPoint is = " + plagiarismPoint * 100 + "%");
                // TODO new : 將所有lls做處理
                for (String lls : llsArray) {
                    // 1-2. 決定中英文之一句話長度參數
                    if (lls.matches(containsEngRegex)) {
                        sentenceLengthParam = 12;
                    } else {
                        sentenceLengthParam = 6;
                    }
                    // 1-3. 若lls的長度為合理一句話（可載入至字典）
                    if (lls.length() > sentenceLengthParam) {
//                         1-4. 一一檢查（三人成虎）
//                        System.out.println("lls : "+lls);
                        if (!shouldFixPlagiarismPointByDict(lls)) { // TODO new : 透過字典，確認是否需要修正抄襲指數
                            if (threeTiger(lls, noteIDArray)) {
                                //TODO new : 確認為引用，且已經寫入至字典
                                System.out.println("已透過與其他文章比較後確認是相似跟引用(三人成虎 check)，抄襲指數需要修改");
                                System.out.println("----------------------------修改後----------------------------");
                                //TODO : 修改抄襲指數 -> totalTextEqual扣掉lls的長度
                                totalTextEqual -= lls.length();
                                plagiarismPoint = plagiarismChecker.getPlagiarism(totalTextEqual, self.length());
//                                System.out.println("文章中共有"+lls.length()+"個字被認為是相似或引用。");
//                                System.out.println("文章中共有"+totalTextEqual+"個字被認為是抄襲。");
//                            System.out.println(self.length() + "字有" + totalTextEqual + "字疑似抄襲，" + lls.length() + "字疑似引用");
//                            System.out.println("抄襲指數為：" + plagiarismPoint * 100 + "%");
                                if (plagiarismPoint > maxPoint) {
                                    maxPoint = plagiarismPoint;
                                    maxTotalTextEqual = totalTextEqual;
                                    maxLls = lls.length();
                                }
//                            result.put("point", plagiarismPoint * 100);
                            }
//                        else {
//                            // TODO new : 已確認不是相似跟引用，抄襲指數不用修改
////                            System.out.println("已確認不是相似跟引用，抄襲指數不用修改");
////                            result.put("point", 0F);
//                        }
//                        result.put("totalTextEqual", totalTextEqual);
//                        result.put("lls", (float) lls.length());
//                        return result;
                        } else {
                            // TODO new : 已確認是相似或引用，抄襲指數需用修改
//                        System.out.println("已透過字典找到相同後確認是相似跟引用，抄襲指數需要修改");
//                        System.out.println("----------------------------修改後----------------------------");
                            //TODO : 修改抄襲指數
                            result.replace("tiger", 1F);
                            totalTextEqual -= lls.length();
                            plagiarismPoint = plagiarismChecker.getPlagiarism(totalTextEqual, self.length());
//                            System.out.println("文章中共有"+lls.length()+"個字被認為是相似或引用。");
//                            System.out.println("文章中共有"+totalTextEqual+"個字被認為是抄襲。");
//                        System.out.println(self.length() + "字有" + totalTextEqual + "字疑似抄襲，" + lls.length() + "字疑似引用");
//                        System.out.println("抄襲指數為：" + plagiarismPoint * 100 + "%");
                            if (plagiarismPoint > maxPoint) {
                                maxPoint = plagiarismPoint;
                                maxTotalTextEqual = totalTextEqual;
                                maxLls = lls.length();
                            }
//                        result.put("point", plagiarismPoint * 100);
//                        result.put("totalTextEqual", totalTextEqual);
//                        result.put("lls", (float) lls.length());
//                        return result;
//                            break;
                        }
                    }
//                else {
//                    System.out.println("lls之長度不符合最低標準：" + sentenceLengthParam);
//                }
                }
            }
        }
        result.put("point", maxPoint);
        result.put("totalTextEqual", maxTotalTextEqual);
        result.put("lls", maxLls);
        return result;
    }

    private boolean threeTiger(String lls, ArrayList<String> noteIDArray) {
        // TODO new : 若字典裡沒有，檢查其他比較對象是否也有含此lls，若有，多一個人看到老虎XD
        int tiger = 0; // 三人成“虎”
//        System.out.println("進入三人成虎");
        for (String s2 : noteIDArray) {
//            System.out.println("s2 = "+s2);
//            System.out.println("lls = "+lls);
            if (findInArticle(s2, lls)) {
                tiger++;
            }
        }
//        System.out.println("tiger = " + tiger);
        if (tiger >= 3) {
            // TODO new : 三個人以上說看到老虎了，載入至字典且紀錄權重（幾個人看到老虎）
//            if (!dictionary.exists()) {
//                try (PrintWriter output = new PrintWriter(dictionary)) {
//                    output.println(lls + ":" + tiger + "}\n");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                writeToDict(lls, tiger);
//            }
            Plagiarismdictionary plagiarismdictionary = new Plagiarismdictionary(lls, tiger);
            plagiarismDictionaryRepository.insert(plagiarismdictionary);
            return true;
        } else {
            return false;
        }
    }

    // TODO new : 進一步比較lls間的抄襲演算法調整（名言佳句 -> 100%相似、格式 -> 相似度高），以用來得出是否需要調整抄襲指數
    private boolean shouldFixPlagiarismPointByDict(String lls) {
//        System.out.println("lls in 2 = "+lls);
        if (!findSameInDictionary(lls)) {
            // 與字典內部所有字串一一檢查各lls間的相似度，並依照相似程度判斷引用標準
            if (findLikedInDictionary(lls)) {
//                System.out.println("需要修改的為："+lls);
                return true;
            }
            // 確認都小於a%，確認不是引用
//            System.out.println("已透過字典認為“不相似”");
            return false;
        } else {
            // 在字典內發現
            return true;
        }
    }

    // TODO new : 與字典內部所有字串一一lls是否存在於字典內
    private boolean findSameInDictionary(String lls) {
        Plagiarismdictionary dbLls = plagiarismDictionaryRepository.findFirstByWord(lls);
        if (dbLls == null) {
            return false;
        } else {
            dbLls.setFrequency(dbLls.getFrequency() + 1);
            plagiarismDictionaryRepository.save(dbLls);
//            System.out.println("已在字典發現，且更新次數");
            return true;
        }
//        Scanner scanner = new Scanner(dictionary);
//        while (scanner.hasNext()) {
//            String tmp = scanner.nextLine();
//            String times = tmp.substring(tmp.lastIndexOf(":") + 1, tmp.length() - 1);
//            String sentence = tmp.substring(0, tmp.lastIndexOf(":"));
////            System.out.println("sentence = "+sentence);
//            if (lls.equals(sentence)) {
////                System.out.println("sentence = "+sentence);
////                System.out.println("equal");
//                int a = Integer.parseInt(times);
//                a += 1;
//                replaceLineFromFile(dictionary.getName(), sentence + ":" + a + "}", sentence + ":" + times + "}");
//                System.out.println("已在字典發現，且更新次數");
//                return true;
//            }
//        }
//        return false;
    }

    // TODO new : 與字典內部所有字串一一檢查各lls間的相似度，並依照相似程度判斷引用標準
    private boolean findLikedInDictionary(String lls) {
        final float a = 0.9F;
        List<Plagiarismdictionary> allLike = plagiarismDictionaryRepository.findAllByWordLike(lls);
        PlagiarismChecker plagiarismChecker = new PlagiarismChecker();
        if (allLike.isEmpty()) {
            return false;
        } else {
            for (Plagiarismdictionary tmp : allLike) {
                String comparison = tmp.getWord();
                float plagiarismPoint = plagiarismChecker.getPlagiarism(plagiarismChecker.getTotalTextEqual(comparison, lls),
                        plagiarismChecker.getArticleLengthDivide(comparison, lls));
                if (plagiarismPoint >= a) {
                    Plagiarismdictionary plagiarismdictionary = new Plagiarismdictionary(lls, 1);
                    plagiarismDictionaryRepository.insert(plagiarismdictionary);
                    return true;
                }
            }
        }
        // 已檢查完畢所有字典，無相似，回傳false(不是引用)
        return false;
//        Scanner scanner = new Scanner(dictionary);
//        PlagiarismChecker plagiarismChecker = new PlagiarismChecker();
//        while (scanner.hasNext()) {
//            String comparison = scanner.nextLine();
//            String times = comparison.substring(comparison.lastIndexOf(":") + 1, comparison.length() - 1);
//            String sentence = comparison.substring(0, comparison.lastIndexOf(":"));
//            float plagiarismPoint = plagiarismChecker.getPlagiarism(plagiarismChecker.getTotalTextEqual(sentence, lls)
//                    , plagiarismChecker.getArticleLengthDivide(sentence, lls));
//            System.out.println("lls in 3 = "+lls);
//            System.out.println("引用指數為 "+plagiarismPoint);
//            if (sentence.trim().equals(lls.trim())) {
//                System.out.println("equal");
//                System.out.println("sentence = "+sentence);
//                int a = Integer.parseInt(times);
//                a += 1;
//                System.out.println("已透過字典認為“相同”，並且更新相似的 lls comparison");
//                System.out.println("lineToRemove : "+sentence+":"+times+"}");
//                System.out.println("lineToReplace : "+sentence+":"+a+"}");
//                replaceLineFromFile(dictionary.getName(), sentence + ":" + a + "}", sentence + ":" + times + "}");
//                return true;
//            }
//            else if (plagiarismPoint > 0.9) {
//                System.out.println("sentence = "+sentence);
//                System.out.println("已透過字典認為“相似”，並且寫入至字典");
//                writeToDict(lls, 1);
//                return true;
//            }
//        }
    }

//    private void replaceLineFromFile(String file, String lineToReplace, String lineToRemove) {
//        try {
//            File inFile = new File(file);
//            if (!inFile.isFile()) {
//                System.out.println("Parameter is not an existing file");
//                return;
//            }
//            //Construct the new file that will later be renamed to the original filename.
//            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
//            String line = null;
//            //Read from the original file and write to the new
//            //unless content matches data to be removed.
//            while ((line = br.readLine()) != null) {
//                if (!line.equals(lineToRemove)) {
//                    pw.println(line);
//                    pw.flush();
//                } else {
//                    pw.println(lineToReplace);
//                    pw.flush();
//                }
//            }
//            pw.close();
//            br.close();
//            //Delete the original file
//            if (!inFile.delete()) {
//                System.out.println("Could not delete file");
//                return;
//            }
//            //Rename the new file to the filename the original file had.
//            if (!tempFile.renameTo(inFile))
//                System.out.println("Could not rename file");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

//    private void writeToDict(String lls, int time) {
//        String tmp = lls + ":" + time + "}\n";
//        try {
//            Files.write(Paths.get(dictionary.getPath()), tmp.getBytes(), StandardOpenOption.APPEND);
//            System.out.println("將 " + tmp + " 寫入至字典");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean findInArticle(String s, String lls) {
        return s.toUpperCase().contains(lls.toUpperCase());
    }

    private String getHtmlCodeInNote(String noteID) {
        System.out.println(noteID);
        Note note = noteRepository.findById(noteID)
                .orElseThrow();
        if (note.getVersion().isEmpty()) {
            return "";
        } else if (note.getVersion().get(0).getContent().get(0).getMycustom_html() == null) {
            return "";
        } else {
            return note.getVersion().get(0).getContent().get(0).getMycustom_html();
        }
    }

}
