import csv
import re
import unicodedata

def katakana_to_hiragana(katakana_str):
    result = []
    for char in katakana_str:
        if char == 'ー':  # 伸ばし棒はそのまま保持
            result.append(char)
        elif '\u30A0' <= char <= '\u30FF':  # カタカナをひらがなに変換
            result.append(chr(ord(char) - 96))
        else:
            result.append(char)
    return ''.join(result)

def normalize_word(word):
    """小文字を大文字に変換して正規化する"""
    result = []
    for char in word:
        if char == 'ぁ': result.append('あ')
        elif char == 'ぃ': result.append('い')
        elif char == 'ぅ': result.append('う')
        elif char == 'ぇ': result.append('え')
        elif char == 'ぉ': result.append('お')
        elif char == 'っ': result.append('つ')
        elif char == 'ゃ': result.append('や')
        elif char == 'ゅ': result.append('ゆ')
        elif char == 'ょ': result.append('よ')
        else: result.append(char)
    return ''.join(result)

def has_diacritical_marks(text):
    # 濁点・半濁点を含む文字のUnicode範囲
    # カタカナの濁点・半濁点付き文字の範囲（伸ばし棒「ー」は除外）
    return any('\u30A0' <= char <= '\u30FF' and 
              (char in 'ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポヴ' or
               char in 'がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽゔ')
              for char in text)

def extract_five_letter_words(input_csv, output_txt):
    five_letter_words = set()
    normalized_words = set()  # 正規化された単語の重複チェック用
    
    with open(input_csv, 'r', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            if len(row) >= 17:  # 少なくとも17列あることを確認
                word = row[15]  # 単語
                pos = row[16]   # 品詞情報
                # カタカナのみの5文字の単語を抽出（濁点・半濁点なし、伸ばし棒は許可）
                if (len(word) == 5 and 
                    re.match(r'^[\u30A0-\u30FF\u30FC]+$', word) and  # 伸ばし棒（\u30FC）を追加
                    pos == '名詞-普通名詞-一般' and
                    not has_diacritical_marks(word)):
                    # カタカナをひらがなに変換
                    hiragana_word = katakana_to_hiragana(word)
                    # 正規化した単語が重複していないかチェック
                    normalized = normalize_word(hiragana_word)
                    if normalized not in normalized_words:
                        normalized_words.add(normalized)
                        five_letter_words.add(hiragana_word)
                    else:
                        print(f"警告: 正規化後に重複する単語をスキップしました: {hiragana_word}")
    
    # 結果をテキストファイルに書き出し
    with open(output_txt, 'w', encoding='utf-8') as txtfile:
        for word in sorted(five_letter_words):
            txtfile.write(word + '\n')
    
    print(f'抽出された単語数: {len(five_letter_words)}')

if __name__ == '__main__':
    input_csv = 'VDRJ_Ver1_1_Research_Top60894.csv'
    output_txt = 'words.txt'
    extract_five_letter_words(input_csv, output_txt)
    print(f'5文字の単語を {output_txt} に抽出しました。') 