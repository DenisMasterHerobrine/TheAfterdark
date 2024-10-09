import os
import json

def remove_value_key_from_json(directory):
    # Проходим по всем файлам и поддиректориям
    for root, _, files in os.walk(directory):
        for filename in files:
            if filename.endswith(".json"):
                filepath = os.path.join(root, filename)
                print(f"Обработка файла: {filepath}")
                try:
                    # Читаем содержимое JSON файла
                    with open(filepath, "r", encoding="utf-8") as file:
                        data = json.load(file)
                except json.JSONDecodeError:
                    print(f"Ошибка декодирования JSON в файле {filepath}. Пропускаем файл.")
                    continue
                
                # Обрабатываем JSON, убирая "value"
                processed_data = process_json(data)

                # Записываем обновленное содержимое обратно в файл
                with open(filepath, "w", encoding="utf-8") as file:
                    json.dump(processed_data, file, indent=4, ensure_ascii=False)
                
                print(f"Файл обработан и сохранён: {filepath}")

def process_json(data):
    # Если это словарь, ищем ключ "value"
    if isinstance(data, dict):
        if "value" in data and isinstance(data["value"], dict):
            print(f"Найден и обработан ключ 'value' в {data}")
            # Перемещаем содержимое "value" на уровень выше и удаляем сам "value"
            value_content = data.pop("value")
            data.update(value_content)
        # Рекурсивно обрабатываем все значения словаря
        for key, value in data.items():
            data[key] = process_json(value)
    # Если это список, обрабатываем каждый элемент списка
    elif isinstance(data, list):
        return [process_json(item) for item in data]
    return data

# Укажите путь к директории, содержащей JSON файлы
directory_path = "E:\My Mods\TheAfterdark\common\src\main\\resources\data\\the_afterdark\worldgen"
remove_value_key_from_json(directory_path)
